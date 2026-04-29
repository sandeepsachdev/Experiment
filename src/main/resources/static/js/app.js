/* ═══════════════════════════════════ STATE ══════════════════════════════════ */
let allEvents    = [];
let betSlip      = [];          // { outcomeId, eventName, market, selection, decimalOdds }
let oddsFormat   = 'american';
let activeStatus = 'ALL';
let activeCategory = 'ALL';

/* ═══════════════════════════════════ API ════════════════════════════════════ */
async function fetchEvents() {
  const res = await fetch('/api/events');
  if (!res.ok) throw new Error('Failed to fetch events');
  return res.json();
}

/* ════════════════════════════════ ODDS UTILS ════════════════════════════════ */
function formatOdds(decimal) {
  if (oddsFormat === 'decimal') {
    return decimal.toFixed(2);
  }
  if (oddsFormat === 'fractional') {
    return toFractional(decimal);
  }
  return toAmerican(decimal);
}

function toAmerican(d) {
  if (d >= 2.0) return '+' + Math.round((d - 1) * 100);
  return '' + Math.round(-100 / (d - 1));
}

function toFractional(d) {
  if (Math.abs(d - 2.0) < 0.01) return 'EVS';
  const n = d - 1;
  let bestNum = 1, bestDen = 1, bestErr = Infinity;
  for (let den = 1; den <= 50; den++) {
    const num = Math.round(n * den);
    const err = Math.abs(n - num / den);
    if (err < bestErr) { bestErr = err; bestNum = num; bestDen = den; }
    if (err < 0.0001) break;
  }
  return `${bestNum}/${bestDen}`;
}

/* ═══════════════════════════════ DATE FORMAT ════════════════════════════════ */
function formatDate(isoStr) {
  const d = new Date(isoStr);
  const now = new Date();
  const todayStart = new Date(now.getFullYear(), now.getMonth(), now.getDate());
  const tomorrowStart = new Date(todayStart.getTime() + 86400000);
  const dayAfterStart = new Date(tomorrowStart.getTime() + 86400000);

  const time = d.toLocaleTimeString('en-US', { hour: 'numeric', minute: '2-digit', hour12: true });
  if (d >= todayStart && d < tomorrowStart) return `Today · ${time} ET`;
  if (d >= tomorrowStart && d < dayAfterStart) return `Tomorrow · ${time} ET`;
  return d.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' }) + ` · ${time} ET`;
}

/* ══════════════════════════════ RENDER EVENTS ═══════════════════════════════ */
function renderEvents() {
  const grid = document.getElementById('eventsGrid');
  const title = document.getElementById('feedTitle');
  const countEl = document.getElementById('eventCount');

  let events = allEvents;
  if (activeStatus !== 'ALL') {
    events = events.filter(e => e.status === activeStatus);
  }
  if (activeCategory !== 'ALL') {
    events = events.filter(e => e.category === activeCategory);
  }

  // Live events first, then featured, then chronological
  events = [...events].sort((a, b) => {
    if (a.status === 'LIVE' && b.status !== 'LIVE') return -1;
    if (b.status === 'LIVE' && a.status !== 'LIVE') return  1;
    if (a.featured && !b.featured) return -1;
    if (b.featured && !a.featured) return  1;
    return new Date(a.eventDate) - new Date(b.eventDate);
  });

  // Title
  const catLabel = activeCategory === 'ALL' ? 'All Sports' : activeCategory;
  const statusLabel = activeStatus === 'ALL' ? 'All Events'
    : activeStatus === 'LIVE' ? 'Live Now' : 'Upcoming';
  title.textContent = activeCategory === 'ALL' ? statusLabel : `${catLabel} · ${statusLabel}`;
  countEl.textContent = `${events.length} event${events.length !== 1 ? 's' : ''}`;

  if (!events.length) {
    grid.innerHTML = `
      <div class="empty-state">
        <div class="icon">📭</div>
        <h3>No events found</h3>
        <p>Try changing the filter above.</p>
      </div>`;
    return;
  }

  grid.innerHTML = events.map(e => renderEventCard(e)).join('');
}

function renderEventCard(event) {
  const isLive = event.status === 'LIVE';
  const cardClass = [
    'event-card',
    isLive ? 'is-live' : '',
    event.featured ? 'is-featured' : ''
  ].filter(Boolean).join(' ');

  const statusBadge = isLive
    ? `<span class="badge-live"><span class="live-dot"></span> LIVE</span>`
    : `<span class="badge-upcoming">Upcoming</span>`;

  const featuredBadge = event.featured ? `<span class="badge-featured">⭐ Featured</span>` : '';

  const teamsLine = (event.homeTeam && event.awayTeam)
    ? `<span class="teams-matchup">
         ${escHtml(event.homeTeam)}
         <span class="team-vs">vs</span>
         ${escHtml(event.awayTeam)}
       </span>`
    : `<span class="teams-matchup">${escHtml(event.name)}</span>`;

  const marketsHtml = event.markets.map(m => `
    <div class="market-row">
      <div class="market-name">${escHtml(m.name)}</div>
      <div class="outcomes-row">
        ${m.outcomes.map(o => renderOddsBtn(event, m, o)).join('')}
      </div>
    </div>`).join('');

  return `
    <div class="${cardClass}" data-event-id="${event.id}">
      <div class="card-header">
        <div class="card-meta">
          <span class="card-category">${escHtml(event.category)}</span>
          <span class="card-category" style="color:var(--border-hover)">·</span>
          <span class="card-competition">${escHtml(event.competition)}</span>
        </div>
        <div class="card-badges">
          ${featuredBadge}
          ${statusBadge}
        </div>
      </div>
      <div class="card-teams">
        ${teamsLine}
        <div class="card-date">${isLive ? '🔴 In Progress' : formatDate(event.eventDate)}</div>
      </div>
      <div class="card-markets">${marketsHtml}</div>
    </div>`;
}

function renderOddsBtn(event, market, outcome) {
  const isSelected = betSlip.some(b => b.outcomeId === outcome.id);
  const cls = isSelected ? 'odds-btn selected' : 'odds-btn';
  return `
    <button class="${cls}"
            data-outcome-id="${outcome.id}"
            data-event-id="${event.id}"
            data-event-name="${escAttr(event.name)}"
            data-market="${escAttr(market.name)}"
            data-selection="${escAttr(outcome.name)}"
            data-odds="${outcome.decimalOdds}">
      <span class="odds-selection">${escHtml(outcome.name)}</span>
      <span class="odds-value">${formatOdds(outcome.decimalOdds)}</span>
    </button>`;
}

/* ═════════════════════════════ ODDS FORMAT RERENDER ═════════════════════════ */
function rerenderOdds() {
  document.querySelectorAll('.odds-btn').forEach(btn => {
    const decimal = parseFloat(btn.dataset.odds);
    const valueEl = btn.querySelector('.odds-value');
    if (valueEl) valueEl.textContent = formatOdds(decimal);
  });
  renderBetSlip(); // refresh slip odds display too
}

/* ════════════════════════════════ BET SLIP ══════════════════════════════════ */
function toggleBet(outcomeId, eventName, market, selection, decimalOdds) {
  const idx = betSlip.findIndex(b => b.outcomeId === outcomeId);
  if (idx !== -1) {
    betSlip.splice(idx, 1);
  } else {
    betSlip.push({ outcomeId, eventName, market, selection, decimalOdds });
  }
  renderBetSlip();
  updateBetslipCount();
}

function renderBetSlip() {
  const itemsEl  = document.getElementById('betslipItems');
  const footerEl = document.getElementById('betslipFooter');

  if (!betSlip.length) {
    itemsEl.innerHTML = `<div class="betslip-empty">No selections yet.<br>Click any odds to add a bet.</div>`;
    footerEl.style.display = 'none';
    return;
  }

  itemsEl.innerHTML = betSlip.map((b, i) => `
    <div class="slip-item">
      <div class="slip-event">${escHtml(b.eventName)}</div>
      <div class="slip-market">${escHtml(b.market)}</div>
      <div class="slip-row">
        <span class="slip-selection">${escHtml(b.selection)}</span>
        <span class="slip-odds">${formatOdds(b.decimalOdds)}</span>
      </div>
      <button class="slip-remove" data-slip-index="${i}">✕</button>
    </div>`).join('');

  footerEl.style.display = 'block';

  // Total decimal odds (accumulator)
  const totalDecimal = betSlip.reduce((acc, b) => acc * b.decimalOdds, 1);
  document.getElementById('betslipTotalOdds').textContent = formatOdds(totalDecimal);

  // Payout calculation
  const stake = parseFloat(document.getElementById('stakeInput').value) || 0;
  if (stake > 0) {
    const payout = (stake * totalDecimal).toFixed(2);
    document.getElementById('payoutValue').textContent = `$${payout}`;
  } else {
    document.getElementById('payoutValue').textContent = '—';
  }
}

function updateBetslipCount() {
  const count = betSlip.length;
  const countEl = document.getElementById('betslipCount');
  countEl.textContent = count;
  countEl.classList.toggle('hidden', count === 0);
}

function clearSlip() {
  betSlip = [];
  renderBetSlip();
  updateBetslipCount();
  // De-select all odds buttons
  document.querySelectorAll('.odds-btn.selected').forEach(btn => btn.classList.remove('selected'));
}

/* ═══════════════════════════════ INIT & EVENTS ══════════════════════════════ */
async function init() {
  document.getElementById('eventsGrid').innerHTML =
    `<div class="loading"><div class="spinner"></div> Loading events…</div>`;

  try {
    allEvents = await fetchEvents();
  } catch (err) {
    document.getElementById('eventsGrid').innerHTML = `
      <div class="empty-state">
        <div class="icon">⚠️</div>
        <h3>Could not load events</h3>
        <p>${escHtml(err.message)}</p>
      </div>`;
    return;
  }

  renderEvents();
  setupListeners();
}

function setupListeners() {
  // ── Status nav ────────────────────────────────────────────────────────────
  document.getElementById('statusNav').addEventListener('click', e => {
    const btn = e.target.closest('.snav-btn');
    if (!btn) return;
    document.querySelectorAll('#statusNav .snav-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
    activeStatus = btn.dataset.status;
    renderEvents();
  });

  // ── Sport nav ─────────────────────────────────────────────────────────────
  document.getElementById('sportNav').addEventListener('click', e => {
    const btn = e.target.closest('.snav-btn');
    if (!btn) return;
    document.querySelectorAll('#sportNav .snav-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
    activeCategory = btn.dataset.category;
    renderEvents();
  });

  // ── Odds format toggle ────────────────────────────────────────────────────
  document.getElementById('oddsFormatGroup').addEventListener('click', e => {
    const btn = e.target.closest('.toggle-btn');
    if (!btn) return;
    document.querySelectorAll('.toggle-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
    oddsFormat = btn.dataset.format;
    rerenderOdds();
  });

  // ── Odds buttons (event delegation on grid) ───────────────────────────────
  document.getElementById('eventsGrid').addEventListener('click', e => {
    const btn = e.target.closest('.odds-btn');
    if (!btn) return;
    const { outcomeId, eventName, market, selection, odds } = btn.dataset;
    toggleBet(
      parseInt(outcomeId, 10),
      eventName,
      market,
      selection,
      parseFloat(odds)
    );
    btn.classList.toggle('selected');
    const valueEl = btn.querySelector('.odds-value');
    if (valueEl) valueEl.textContent = formatOdds(parseFloat(odds));
  });

  // ── Bet slip: remove individual bet ──────────────────────────────────────
  document.getElementById('betslipItems').addEventListener('click', e => {
    const removeBtn = e.target.closest('.slip-remove');
    if (!removeBtn) return;
    const idx = parseInt(removeBtn.dataset.slipIndex, 10);
    const removed = betSlip.splice(idx, 1)[0];
    // De-select the corresponding odds button in the grid
    const oddsBtn = document.querySelector(`.odds-btn[data-outcome-id="${removed.outcomeId}"]`);
    if (oddsBtn) oddsBtn.classList.remove('selected');
    renderBetSlip();
    updateBetslipCount();
  });

  // ── Stake input → recalculate payout ─────────────────────────────────────
  document.getElementById('stakeInput').addEventListener('input', renderBetSlip);

  // ── Clear slip ────────────────────────────────────────────────────────────
  document.getElementById('clearSlip').addEventListener('click', clearSlip);

  // ── Place bet (visual only) ────────────────────────────────────────────────
  document.querySelector('.btn-place-bet').addEventListener('click', () => {
    if (!betSlip.length) return;
    alert(`✅ Bet placed! ${betSlip.length} selection${betSlip.length > 1 ? 's' : ''}. Good luck!`);
    clearSlip();
  });

  // ── Bet slip panel open/close (mobile/tablet) ─────────────────────────────
  document.getElementById('betslipTrigger').addEventListener('click', () => {
    document.getElementById('betslipPanel').classList.add('open');
    document.getElementById('betslipOverlay').classList.remove('hidden');
  });

  const closeSlipPanel = () => {
    document.getElementById('betslipPanel').classList.remove('open');
    document.getElementById('betslipOverlay').classList.add('hidden');
  };
  document.getElementById('betslipClose').addEventListener('click', closeSlipPanel);
  document.getElementById('betslipOverlay').addEventListener('click', closeSlipPanel);
}

/* ════════════════════════════════ HELPERS ═══════════════════════════════════ */
function escHtml(s) {
  return String(s ?? '')
    .replace(/&/g, '&amp;').replace(/</g, '&lt;')
    .replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}
function escAttr(s) { return escHtml(s); }

/* ════════════════════════════════ BOOT ══════════════════════════════════════ */
document.addEventListener('DOMContentLoaded', init);
