/* ═══════════════════════════════════ STATE ══════════════════════════════════ */
let currentUser  = null;
let allProfiles  = [];
let browseQueue  = [];
let browseIndex  = 0;
let likedIds     = new Set();
let skippedIds   = new Set();
let currentView  = 'browse';

/* ═══════════════════════════════════ API ════════════════════════════════════ */
async function apiFetch(path, options = {}) {
  const res = await fetch(path, options);
  if (!res.ok) throw new Error(`${res.status} ${res.statusText} — ${path}`);
  return res.json();
}
const getProfiles      = ()     => apiFetch('/api/profiles');
const getLikesSent     = (id)   => apiFetch(`/api/likes/${id}/sent`);
const getLikesReceived = (id)   => apiFetch(`/api/likes/${id}/received`);
const getMatches       = (id)   => apiFetch(`/api/matches/${id}`);
const sendLike         = (a, b) => apiFetch('/api/likes', {
  method:  'POST',
  headers: { 'Content-Type': 'application/json' },
  body:    JSON.stringify({ likerId: a, likedId: b })
});

/* ════════════════════════════════ INIT ══════════════════════════════════════ */
async function init() {
  try {
    allProfiles = await getProfiles();
  } catch (err) {
    showError('Failed to load profiles. Is the server running?');
    return;
  }
  setupGlobalListeners();
  showSelectorScreen();
}

/* ════════════════════════════ ERROR TOAST ═══════════════════════════════════ */
function showError(msg) {
  let toast = document.getElementById('errorToast');
  if (!toast) {
    toast = document.createElement('div');
    toast.id = 'errorToast';
    toast.style.cssText = [
      'position:fixed','bottom:24px','left:50%','transform:translateX(-50%)',
      'background:#EF4444','color:#fff','padding:12px 20px','border-radius:10px',
      'font-size:0.88rem','font-weight:600','z-index:999','box-shadow:0 4px 16px rgba(0,0,0,0.4)',
      'max-width:340px','text-align:center'
    ].join(';');
    document.body.appendChild(toast);
  }
  toast.textContent = msg;
  toast.style.display = 'block';
  setTimeout(() => { toast.style.display = 'none'; }, 4000);
}

/* ══════════════════════════════ PROFILE SELECTOR ════════════════════════════ */
function showSelectorScreen(filterGender = 'ALL') {
  document.getElementById('selectorScreen').classList.remove('hidden');
  document.getElementById('mainApp').classList.add('hidden');
  // Sync the active tab
  document.querySelectorAll('.gender-tab').forEach(t => {
    t.classList.toggle('active', t.dataset.gender === filterGender);
  });
  renderSelectorGrid(filterGender);
}

function renderSelectorGrid(filterGender = 'ALL') {
  const grid = document.getElementById('selectorGrid');
  const profiles = filterGender === 'ALL'
    ? allProfiles
    : allProfiles.filter(p => p.gender === filterGender);

  // Use data-profile-id — no inline onclick needed
  grid.innerHTML = profiles.map(p => `
    <div class="selector-card ${p.gender.toLowerCase()}" data-profile-id="${p.id}">
      <img src="${p.photoUrl}" alt="${p.name}"
           onerror="this.src='https://ui-avatars.com/api/?name=${encodeURIComponent(p.name)}&background=1E1E35&color=A78BFA&size=72'">
      <strong>${escHtml(p.name)}</strong>
      <small>${p.age} · ${p.gender === 'MALE' ? '♂' : '♀'} · ${escHtml(p.location.split(',')[0])}</small>
    </div>`).join('');
}

/* ══════════════════════════════ SELECT USER ═════════════════════════════════ */
async function selectUser(id) {
  const profile = allProfiles.find(p => p.id === id);
  if (!profile) { showError('Profile not found'); return; }

  currentUser  = profile;
  skippedIds   = new Set();

  // Fetch already-liked profiles from server — fail gracefully
  try {
    const sent = await getLikesSent(id);
    likedIds = new Set(Array.isArray(sent) ? sent.map(p => p.id) : []);
  } catch (_) {
    likedIds = new Set();
  }

  // Update sidebar & topbar
  const fallback = avatarUrl(currentUser.name, 80);
  setImgSrc('sidebarAvatar', currentUser.photoUrl, fallback);
  setImgSrc('topbarAvatar',  currentUser.photoUrl, fallback);
  document.getElementById('sidebarName').textContent = currentUser.name;
  document.getElementById('sidebarMeta').textContent =
    `${currentUser.age} · ${currentUser.gender === 'MALE' ? '♂ Male' : '♀ Female'}`;

  // Switch screens
  document.getElementById('selectorScreen').classList.add('hidden');
  document.getElementById('mainApp').classList.remove('hidden');

  buildBrowseQueue();
  updateBadges();          // fire-and-forget
  navigateTo('browse');
}

/* ══════════════════════════════ BROWSE QUEUE ════════════════════════════════ */
function buildBrowseQueue() {
  const target = currentUser.lookingFor;
  browseQueue = allProfiles.filter(p =>
    p.id !== currentUser.id &&
    p.gender === target &&
    !likedIds.has(p.id) &&
    !skippedIds.has(p.id)
  );
  browseIndex = 0;
}

/* ═══════════════════════════════ NAVIGATION ═════════════════════════════════ */
function navigateTo(view) {
  currentView = view;
  document.querySelectorAll('.nav-btn, .bnav-btn').forEach(btn => {
    btn.classList.toggle('active', btn.dataset.view === view);
  });
  switch (view) {
    case 'browse':  renderBrowse();         break;
    case 'matches': renderMatchesView();    break;
    case 'likes':   renderLikesView();      break;
    case 'profile': renderMyProfileView();  break;
  }
}

/* ═══════════════════════════════ BROWSE VIEW ════════════════════════════════ */
function renderBrowse() {
  const el = document.getElementById('mainContent');

  if (!browseQueue.length || browseIndex >= browseQueue.length) {
    el.innerHTML = `
      <div class="browse-view">
        <div class="browse-header"><h2>Discover</h2></div>
        <div class="empty-state">
          <div class="empty-icon">🌸</div>
          <h3>You've seen everyone!</h3>
          <p>No more new profiles right now. Start over to see them again.</p>
          <button class="btn btn-primary" data-action="reset-browse">Start Over</button>
        </div>
      </div>`;
    return;
  }

  const p = browseQueue[browseIndex];
  const fb = avatarUrl(p.name, 400);

  el.innerHTML = `
    <div class="browse-view">
      <div class="browse-header">
        <h2>Discover</h2>
        <span class="browse-counter">${browseIndex + 1} / ${browseQueue.length}</span>
      </div>
      <div class="profile-card-lg animate-in">
        <div class="card-photo-wrap">
          <img src="${p.photoUrl}" alt="${escHtml(p.name)}"
               onerror="this.src='${fb}'">
          <div class="card-photo-gradient"></div>
          <div class="card-photo-info">
            <div class="card-photo-name">${escHtml(p.name)}, ${p.age}</div>
            <div class="card-photo-location">📍 ${escHtml(p.location)}</div>
          </div>
        </div>
        <div class="card-body">
          <p class="card-bio">${escHtml(p.bio)}</p>
          <div class="tags">${tagsHtml(p.interests)}</div>
        </div>
        <div class="card-actions">
          <button class="btn-pass" data-action="skip"  aria-label="Pass">✕</button>
          <button class="btn-heart" data-action="like" aria-label="Like">❤️</button>
        </div>
      </div>
    </div>`;
}

/* ══════════════════════════════ LIKE / SKIP ═════════════════════════════════ */
async function handleLike() {
  if (browseIndex >= browseQueue.length) return;
  const liked = browseQueue[browseIndex];
  likedIds.add(liked.id);
  browseIndex++;
  renderBrowse();

  try {
    const result = await sendLike(currentUser.id, liked.id);
    if (result.matched) {
      showMatchModal(liked);
      updateBadges();
    }
  } catch (err) {
    console.error('Like failed:', err);
  }
}

function handleSkip() {
  if (browseIndex >= browseQueue.length) return;
  skippedIds.add(browseQueue[browseIndex].id);
  browseIndex++;
  renderBrowse();
}

function resetBrowse() {
  skippedIds = new Set();
  buildBrowseQueue();
  renderBrowse();
}

/* ═════════════════════════════ MATCHES VIEW ═════════════════════════════════ */
async function renderMatchesView() {
  const el = document.getElementById('mainContent');
  el.innerHTML = loadingHtml();

  let matches;
  try { matches = await getMatches(currentUser.id); }
  catch (_) { matches = []; showError('Could not load matches'); }

  if (!matches.length) {
    el.innerHTML = `
      <div class="section-header"><h2>💞 Matches</h2></div>
      <div class="empty-state">
        <div class="empty-icon">💔</div>
        <h3>No matches yet</h3>
        <p>Keep browsing — your match is out there!</p>
        <button class="btn btn-primary" data-action="nav" data-view="browse">Start Browsing</button>
      </div>`;
    return;
  }

  el.innerHTML = `
    <div class="section-header">
      <h2>💞 Matches</h2>
      <span class="badge">${matches.length}</span>
    </div>
    <div class="profiles-grid">
      ${matches.map(p => gridCardHtml(p,
        `<button class="btn-message" data-action="message">💬 Message</button>`
      )).join('')}
    </div>`;
}

/* ═══════════════════════════════ LIKES VIEW ═════════════════════════════════ */
async function renderLikesView() {
  const el = document.getElementById('mainContent');
  el.innerHTML = loadingHtml();

  let likers, matches;
  try {
    [likers, matches] = await Promise.all([
      getLikesReceived(currentUser.id),
      getMatches(currentUser.id)
    ]);
  } catch (_) {
    likers = []; matches = [];
    showError('Could not load likes');
  }

  const matchedIds = new Set(matches.map(m => m.id));

  if (!likers.length) {
    el.innerHTML = `
      <div class="section-header"><h2>⭐ Liked Me</h2></div>
      <div class="empty-state">
        <div class="empty-icon">🌟</div>
        <h3>No admirers yet</h3>
        <p>People who like your profile will appear here.</p>
      </div>`;
    return;
  }

  el.innerHTML = `
    <div class="section-header">
      <h2>⭐ Liked Me</h2>
      <span class="badge">${likers.length}</span>
    </div>
    <div class="profiles-grid">
      ${likers.map(p => {
        const already = matchedIds.has(p.id) || likedIds.has(p.id);
        const btn = already
          ? `<button class="btn-like-back matched" disabled>${matchedIds.has(p.id) ? '💞 Matched!' : '❤️ Liked!'}</button>`
          : `<button class="btn-like-back" data-action="like-back" data-id="${p.id}">❤️ Like Back</button>`;
        return gridCardHtml(p, btn);
      }).join('')}
    </div>`;
}

async function likeBack(likedId, btn) {
  btn.disabled    = true;
  btn.textContent = '…';
  try {
    const result = await sendLike(currentUser.id, likedId);
    likedIds.add(likedId);
    if (result.matched) {
      btn.textContent = '💞 Matched!';
      btn.classList.add('matched');
      showMatchModal(allProfiles.find(p => p.id === likedId));
      updateBadges();
    } else {
      btn.textContent = '❤️ Liked!';
      btn.classList.add('matched');
    }
  } catch (_) {
    btn.disabled    = false;
    btn.textContent = '❤️ Like Back';
    showError('Could not send like. Try again.');
  }
}

/* ══════════════════════════════ MY PROFILE VIEW ═════════════════════════════ */
function renderMyProfileView() {
  const el = document.getElementById('mainContent');
  const p  = currentUser;
  const fb = avatarUrl(p.name, 400);
  const gPill = p.gender === 'MALE'
    ? `<span class="pill pill-male">♂ Male</span>`
    : `<span class="pill pill-female">♀ Female</span>`;
  const lPill = p.lookingFor === 'MALE'
    ? `<span class="pill pill-looking">Looking for ♂ Men</span>`
    : `<span class="pill pill-looking">Looking for ♀ Women</span>`;

  el.innerHTML = `
    <div class="section-header"><h2>👤 My Profile</h2></div>
    <div class="my-profile-view">
      <div class="profile-card-lg animate-in">
        <div class="card-photo-wrap">
          <img src="${p.photoUrl}" alt="${escHtml(p.name)}" onerror="this.src='${fb}'">
          <div class="card-photo-gradient"></div>
          <div class="card-photo-info">
            <div class="card-photo-name">${escHtml(p.name)}, ${p.age}</div>
            <div class="card-photo-location">📍 ${escHtml(p.location)}</div>
          </div>
        </div>
        <div class="card-body">
          <div class="profile-details-meta">${gPill} ${lPill}</div>
          <p class="card-bio">${escHtml(p.bio)}</p>
          <div class="tags">${tagsHtml(p.interests)}</div>
        </div>
      </div>
    </div>`;
}

/* ══════════════════════════════ MATCH MODAL ═════════════════════════════════ */
function showMatchModal(them) {
  if (!them) return;
  setImgSrc('matchPhotoA', currentUser.photoUrl, avatarUrl(currentUser.name, 88));
  setImgSrc('matchPhotoB', them.photoUrl,        avatarUrl(them.name, 88));
  document.getElementById('matchSubtext').textContent =
    `You and ${them.name} liked each other!`;
  document.getElementById('matchModal').classList.remove('hidden');
}

function closeMatch() {
  document.getElementById('matchModal').classList.add('hidden');
}

/* ══════════════════════════════ BADGE UPDATES ═══════════════════════════════ */
async function updateBadges() {
  try {
    const [matches, received] = await Promise.all([
      getMatches(currentUser.id),
      getLikesReceived(currentUser.id)
    ]);
    setBadge('matchBadge', matches.length);
    setBadge('likeBadge',  received.length);
  } catch (_) { /* non-critical */ }
}

function setBadge(id, count) {
  const el = document.getElementById(id);
  if (!el) return;
  el.textContent = count;
  el.classList.toggle('hidden', count === 0);
}

/* ════════════════════════════ EVENT DELEGATION ══════════════════════════════ */
function setupGlobalListeners() {
  // ── Selector screen: profile card clicks ──────────────────────────────────
  document.getElementById('selectorGrid').addEventListener('click', e => {
    const card = e.target.closest('[data-profile-id]');
    if (!card) return;
    selectUser(parseInt(card.dataset.profileId, 10));
  });

  // ── Selector screen: gender filter tabs ───────────────────────────────────
  document.querySelector('.gender-tabs').addEventListener('click', e => {
    const tab = e.target.closest('.gender-tab');
    if (!tab) return;
    renderSelectorGrid(tab.dataset.gender);
    document.querySelectorAll('.gender-tab').forEach(t => t.classList.remove('active'));
    tab.classList.add('active');
  });

  // ── Main content: browse actions, likes, navigation ───────────────────────
  document.getElementById('mainContent').addEventListener('click', e => {
    const btn = e.target.closest('[data-action]');
    if (!btn) return;
    const { action, id, view } = btn.dataset;
    switch (action) {
      case 'like':         handleLike(); break;
      case 'skip':         handleSkip(); break;
      case 'reset-browse': resetBrowse(); break;
      case 'like-back':    likeBack(parseInt(id, 10), btn); break;
      case 'message':      showError('Messaging coming soon! 💬'); break;
      case 'nav':          if (view) navigateTo(view); break;
    }
  });

  // ── Sidebar nav ───────────────────────────────────────────────────────────
  document.getElementById('sidebarNav').addEventListener('click', e => {
    const btn = e.target.closest('.nav-btn');
    if (btn?.dataset.view) navigateTo(btn.dataset.view);
  });

  // ── Bottom nav (mobile) ───────────────────────────────────────────────────
  document.querySelector('.bottom-nav').addEventListener('click', e => {
    const btn = e.target.closest('.bnav-btn');
    if (btn?.dataset.view) navigateTo(btn.dataset.view);
  });

  // ── Switch profile buttons ────────────────────────────────────────────────
  document.getElementById('switchProfileBtn').addEventListener('click',
    () => showSelectorScreen());
  document.getElementById('topbarSwitchBtn').addEventListener('click',
    () => showSelectorScreen());

  // ── Match modal ───────────────────────────────────────────────────────────
  document.getElementById('matchContinueBtn').addEventListener('click', closeMatch);
  document.getElementById('matchModal').addEventListener('click', e => {
    if (e.target === e.currentTarget) closeMatch();
  });
}

/* ═══════════════════════════════ HELPERS ════════════════════════════════════ */
function escHtml(str) {
  return String(str)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;');
}

function avatarUrl(name, size) {
  return `https://ui-avatars.com/api/?name=${encodeURIComponent(name)}&background=1E1E35&color=A78BFA&size=${size}`;
}

function setImgSrc(id, src, fallback) {
  const img = document.getElementById(id);
  if (!img) return;
  img.onerror = () => { img.src = fallback; img.onerror = null; };
  img.src = src || fallback;
}

function tagsHtml(interests, cls = '') {
  if (!interests) return '';
  return interests.split(',')
    .map(t => `<span class="tag ${cls}">${escHtml(t.trim())}</span>`)
    .join('');
}

function gridCardHtml(p, actionHtml) {
  const fb = avatarUrl(p.name, 190);
  return `
    <div class="grid-card">
      <div class="grid-card-photo">
        <img src="${p.photoUrl}" alt="${escHtml(p.name)}" onerror="this.src='${fb}'">
      </div>
      <div class="grid-card-body">
        <div class="grid-card-name">${escHtml(p.name)}, ${p.age}</div>
        <div class="grid-card-location">📍 ${escHtml(p.location)}</div>
        <div class="grid-card-tags">
          ${tagsHtml(p.interests.split(',').slice(0, 3).join(','), 'sm')}
        </div>
        <div class="grid-card-action">${actionHtml}</div>
      </div>
    </div>`;
}

function loadingHtml() {
  return `<div class="loading"><div class="spinner"></div> Loading…</div>`;
}

/* ════════════════════════════════ BOOT ══════════════════════════════════════ */
document.addEventListener('DOMContentLoaded', init);
