/* ═══════════════════════════════════ STATE ══════════════════════════════════ */
let currentUser   = null;
let allProfiles   = [];
let browseQueue   = [];
let browseIndex   = 0;
let likedIds      = new Set();   // server-confirmed likes sent by currentUser
let skippedIds    = new Set();   // client-only "pass" tracking this session
let currentView   = 'browse';

/* ═══════════════════════════════════ API ════════════════════════════════════ */
const api = {
  async get(path)        { return fetch(path).then(r => r.json()); },
  async post(path, body) {
    return fetch(path, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body)
    }).then(r => r.json());
  }
};

const getProfiles       = ()     => api.get('/api/profiles');
const getLikesSent      = (id)   => api.get(`/api/likes/${id}/sent`);
const getLikesReceived  = (id)   => api.get(`/api/likes/${id}/received`);
const getMatches        = (id)   => api.get(`/api/matches/${id}`);
const sendLike          = (a, b) => api.post('/api/likes', { likerId: a, likedId: b });

/* ════════════════════════════════ INIT ══════════════════════════════════════ */
async function init() {
  allProfiles = await getProfiles();
  showSelectorScreen();
  setupGlobalListeners();
}

/* ══════════════════════════════ PROFILE SELECTOR ════════════════════════════ */
function showSelectorScreen(filterGender = 'ALL') {
  document.getElementById('selectorScreen').classList.remove('hidden');
  document.getElementById('mainApp').classList.add('hidden');
  renderSelectorGrid(filterGender);
}

function renderSelectorGrid(filterGender = 'ALL') {
  const grid = document.getElementById('selectorGrid');
  const profiles = filterGender === 'ALL'
    ? allProfiles
    : allProfiles.filter(p => p.gender === filterGender);

  grid.innerHTML = profiles.map(p => `
    <div class="selector-card ${p.gender.toLowerCase()}" onclick="selectUser(${p.id})">
      <img src="${p.photoUrl}" alt="${p.name}"
           onerror="this.src='https://ui-avatars.com/api/?name=${encodeURIComponent(p.name)}&background=1E1E35&color=A78BFA&size=72'">
      <strong>${p.name}</strong>
      <small>${p.age} · ${p.gender === 'MALE' ? '♂' : '♀'} · ${p.location.split(',')[0]}</small>
    </div>
  `).join('');
}

/* ══════════════════════════════ SELECT USER ═════════════════════════════════ */
async function selectUser(id) {
  currentUser = allProfiles.find(p => p.id === id);
  skippedIds  = new Set();

  // Fetch which profiles the user has already liked from the server
  const sent = await getLikesSent(id);
  likedIds    = new Set(sent.map(p => p.id));

  // Update sidebar / topbar
  const avatar = currentUser.photoUrl;
  const fallback = `https://ui-avatars.com/api/?name=${encodeURIComponent(currentUser.name)}&background=1E1E35&color=A78BFA&size=80`;

  document.getElementById('sidebarAvatar').src    = avatar;
  document.getElementById('sidebarAvatar').onerror = function() { this.src = fallback; };
  document.getElementById('sidebarName').textContent = currentUser.name;
  document.getElementById('sidebarMeta').textContent = `${currentUser.age} · ${currentUser.gender === 'MALE' ? '♂ Male' : '♀ Female'}`;
  document.getElementById('topbarAvatar').src    = avatar;
  document.getElementById('topbarAvatar').onerror = function() { this.src = fallback; };

  // Show main app
  document.getElementById('selectorScreen').classList.add('hidden');
  document.getElementById('mainApp').classList.remove('hidden');

  buildBrowseQueue();
  await updateBadges();
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

  // Sync all nav buttons (sidebar + bottom)
  document.querySelectorAll('.nav-btn, .bnav-btn').forEach(btn => {
    btn.classList.toggle('active', btn.dataset.view === view);
  });

  switch (view) {
    case 'browse':  renderBrowse();          break;
    case 'matches': renderMatchesView();     break;
    case 'likes':   renderLikesView();       break;
    case 'profile': renderMyProfileView();   break;
  }
}

/* ═══════════════════════════════ BROWSE VIEW ════════════════════════════════ */
function renderBrowse() {
  const el = document.getElementById('mainContent');

  if (browseQueue.length === 0 || browseIndex >= browseQueue.length) {
    el.innerHTML = `
      <div class="browse-view">
        <div class="browse-header">
          <h2>Discover</h2>
        </div>
        <div class="empty-state">
          <div class="empty-icon">🌸</div>
          <h3>You've seen everyone!</h3>
          <p>No more new profiles right now. Check back later or refresh to start over.</p>
          <button class="btn btn-primary" onclick="resetBrowse()">Start Over</button>
        </div>
      </div>`;
    return;
  }

  const p       = browseQueue[browseIndex];
  const total   = browseQueue.length;
  const current = browseIndex + 1;
  const fallback = `https://ui-avatars.com/api/?name=${encodeURIComponent(p.name)}&background=1E1E35&color=A78BFA&size=400`;

  el.innerHTML = `
    <div class="browse-view">
      <div class="browse-header">
        <h2>Discover</h2>
        <span class="browse-counter">${current} / ${total}</span>
      </div>
      <div class="profile-card-lg animate-in">
        <div class="card-photo-wrap">
          <img src="${p.photoUrl}" alt="${p.name}"
               onerror="this.src='${fallback}'">
          <div class="card-photo-gradient"></div>
          <div class="card-photo-info">
            <div class="card-photo-name">${p.name}, ${p.age}</div>
            <div class="card-photo-location">📍 ${p.location}</div>
          </div>
        </div>
        <div class="card-body">
          <p class="card-bio">${p.bio}</p>
          <div class="tags">
            ${tagsHtml(p.interests)}
          </div>
        </div>
        <div class="card-actions">
          <button class="btn-pass" id="btnPass" title="Pass">✕</button>
          <button class="btn-heart" id="btnHeart" title="Like">❤️</button>
        </div>
      </div>
    </div>`;

  document.getElementById('btnHeart').addEventListener('click', handleLike);
  document.getElementById('btnPass').addEventListener('click',  handleSkip);
}

/* ══════════════════════════════ LIKE / SKIP ═════════════════════════════════ */
async function handleLike() {
  if (browseIndex >= browseQueue.length) return;
  const liked = browseQueue[browseIndex];

  // Optimistically advance
  likedIds.add(liked.id);
  browseIndex++;
  renderBrowse();

  try {
    const result = await sendLike(currentUser.id, liked.id);
    if (result.matched) {
      showMatchModal(liked);
      await updateBadges();
    }
  } catch (err) {
    console.error('Like failed', err);
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

  const matches = await getMatches(currentUser.id);

  if (matches.length === 0) {
    el.innerHTML = `
      <div class="section-header"><h2>💞 Matches</h2></div>
      <div class="empty-state">
        <div class="empty-icon">💔</div>
        <h3>No matches yet</h3>
        <p>Keep browsing — your match is out there!</p>
        <button class="btn btn-primary" onclick="navigateTo('browse')">Start Browsing</button>
      </div>`;
    return;
  }

  el.innerHTML = `
    <div class="section-header">
      <h2>💞 Matches</h2>
      <span class="badge">${matches.length}</span>
    </div>
    <div class="profiles-grid">
      ${matches.map(p => gridCardHtml(p, `
        <button class="btn-message" onclick="alert('Messaging coming soon! 💬')">💬 Message</button>
      `)).join('')}
    </div>`;
}

/* ═══════════════════════════════ LIKES VIEW ═════════════════════════════════ */
async function renderLikesView() {
  const el = document.getElementById('mainContent');
  el.innerHTML = loadingHtml();

  const [likers, matches] = await Promise.all([
    getLikesReceived(currentUser.id),
    getMatches(currentUser.id)
  ]);

  const matchedIds = new Set(matches.map(m => m.id));

  if (likers.length === 0) {
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
        const isMatch   = matchedIds.has(p.id);
        const alreadyLiked = likedIds.has(p.id);
        const actionBtn = isMatch
          ? `<button class="btn-like-back matched" disabled>💞 Matched!</button>`
          : alreadyLiked
            ? `<button class="btn-like-back matched" disabled>❤️ Liked!</button>`
            : `<button class="btn-like-back" data-id="${p.id}" onclick="likeBack(${p.id}, this)">❤️ Like Back</button>`;
        return gridCardHtml(p, actionBtn);
      }).join('')}
    </div>`;
}

async function likeBack(likedId, btn) {
  btn.disabled   = true;
  btn.textContent = '…';

  try {
    const result = await sendLike(currentUser.id, likedId);
    likedIds.add(likedId);

    if (result.matched) {
      btn.textContent = '💞 Matched!';
      btn.classList.add('matched');
      const them = allProfiles.find(p => p.id === likedId);
      showMatchModal(them);
      await updateBadges();
    } else {
      btn.textContent = '❤️ Liked!';
      btn.classList.add('matched');
    }
  } catch (err) {
    btn.disabled   = false;
    btn.textContent = '❤️ Like Back';
  }
}

/* ══════════════════════════════ MY PROFILE VIEW ═════════════════════════════ */
function renderMyProfileView() {
  const el = document.getElementById('mainContent');
  const p  = currentUser;
  const fallback = `https://ui-avatars.com/api/?name=${encodeURIComponent(p.name)}&background=1E1E35&color=A78BFA&size=400`;

  const genderPill  = p.gender === 'MALE'
    ? `<span class="pill pill-male">♂ Male</span>`
    : `<span class="pill pill-female">♀ Female</span>`;
  const lookingPill = p.lookingFor === 'MALE'
    ? `<span class="pill pill-looking">Looking for ♂ Men</span>`
    : `<span class="pill pill-looking">Looking for ♀ Women</span>`;

  el.innerHTML = `
    <div class="section-header"><h2>👤 My Profile</h2></div>
    <div class="my-profile-view">
      <div class="profile-card-lg animate-in">
        <div class="card-photo-wrap">
          <img src="${p.photoUrl}" alt="${p.name}"
               onerror="this.src='${fallback}'">
          <div class="card-photo-gradient"></div>
          <div class="card-photo-info">
            <div class="card-photo-name">${p.name}, ${p.age}</div>
            <div class="card-photo-location">📍 ${p.location}</div>
          </div>
        </div>
        <div class="card-body">
          <div class="profile-details-meta">
            ${genderPill} ${lookingPill}
          </div>
          <p class="card-bio">${p.bio}</p>
          <div class="tags">${tagsHtml(p.interests)}</div>
        </div>
      </div>
    </div>`;
}

/* ══════════════════════════════ MATCH MODAL ═════════════════════════════════ */
function showMatchModal(them) {
  const fallbackA = `https://ui-avatars.com/api/?name=${encodeURIComponent(currentUser.name)}&background=1E1E35&color=A78BFA&size=88`;
  const fallbackB = `https://ui-avatars.com/api/?name=${encodeURIComponent(them.name)}&background=1E1E35&color=A78BFA&size=88`;

  document.getElementById('matchPhotoA').src    = currentUser.photoUrl;
  document.getElementById('matchPhotoA').onerror = function() { this.src = fallbackA; };
  document.getElementById('matchPhotoB').src    = them.photoUrl;
  document.getElementById('matchPhotoB').onerror = function() { this.src = fallbackB; };
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

/* ═══════════════════════════════ HELPERS ════════════════════════════════════ */
function tagsHtml(interests, cls = '') {
  if (!interests) return '';
  return interests.split(',')
    .map(t => `<span class="tag ${cls}">${t.trim()}</span>`)
    .join('');
}

function gridCardHtml(p, actionHtml) {
  const fallback = `https://ui-avatars.com/api/?name=${encodeURIComponent(p.name)}&background=1E1E35&color=A78BFA&size=190`;
  return `
    <div class="grid-card">
      <div class="grid-card-photo">
        <img src="${p.photoUrl}" alt="${p.name}"
             onerror="this.src='${fallback}'">
      </div>
      <div class="grid-card-body">
        <div class="grid-card-name">${p.name}, ${p.age}</div>
        <div class="grid-card-location">📍 ${p.location}</div>
        <div class="grid-card-tags">${tagsHtml(p.interests.split(',').slice(0,3).join(','), 'sm')}</div>
        <div class="grid-card-action">${actionHtml}</div>
      </div>
    </div>`;
}

function loadingHtml() {
  return `<div class="loading"><div class="spinner"></div> Loading…</div>`;
}

/* ════════════════════════════ EVENT LISTENERS ═══════════════════════════════ */
function setupGlobalListeners() {
  // Sidebar nav
  document.getElementById('sidebarNav').addEventListener('click', e => {
    const btn = e.target.closest('.nav-btn');
    if (btn) navigateTo(btn.dataset.view);
  });

  // Bottom nav
  document.querySelector('.bottom-nav').addEventListener('click', e => {
    const btn = e.target.closest('.bnav-btn');
    if (btn) navigateTo(btn.dataset.view);
  });

  // Switch profile buttons
  document.getElementById('switchProfileBtn').addEventListener('click',
    () => showSelectorScreen());
  document.getElementById('topbarSwitchBtn').addEventListener('click',
    () => showSelectorScreen());

  // Gender filter tabs in selector
  document.querySelector('.gender-tabs').addEventListener('click', e => {
    const tab = e.target.closest('.gender-tab');
    if (!tab) return;
    document.querySelectorAll('.gender-tab').forEach(t => t.classList.remove('active'));
    tab.classList.add('active');
    renderSelectorGrid(tab.dataset.gender);
  });

  // Match modal: close on backdrop click or button
  document.getElementById('matchModal').addEventListener('click', e => {
    if (e.target === document.getElementById('matchModal')) closeMatch();
  });
  document.getElementById('matchContinueBtn').addEventListener('click', closeMatch);
}

/* ════════════════════════════════ BOOT ══════════════════════════════════════ */
document.addEventListener('DOMContentLoaded', init);
