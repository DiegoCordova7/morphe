document.addEventListener("DOMContentLoaded", () => {
  loadHeader();
  loadFooter();
  setActiveNav();
});

function loadHeader() {
  const container = document.getElementById("site-header");
  if (!container) return;

  container.innerHTML = `
    <nav>
      <a href="index.html" class="logo">
        <img src="./img/morphe-mark.svg" alt="Morphe Logo" class="logo-icon" />
        <span>Morphe</span>
      </a>
      <div class="nav-links">
        <a href="index.html" data-page="index.html">Home</a>
        <a href="manifesto.html" data-page="manifesto.html">Manifesto</a>
        <a href="syntax.html" data-page="syntax.html">Syntax</a>
        <a href="playground.html" data-page="playground.html">Playground</a>
        <a href="https://github.com/DiegoCordova7/morphe-docs" target="_blank">GitHub</a>
      </div>
    </nav>
  `;
}

function loadFooter() {
  const container = document.getElementById("site-footer");
  if (!container) return;

  const chips = [
    ["kw", "type"],
    ["ty", "City"],
    ["kw", "match"],
    ["op", "=>"],
    ["st", '"Moctezuma"'],
    ["kw", "func"],
    ["fn", "main"],
    ["ty", "Void"],
    ["kw", "new"],
    ["op", "~="],
    ["kw", "attempt"],
    ["kw", "onError"],
    ["op", ".."],
  ];

  const group = chips
    .map(([cls, text]) => `<span class="m-${cls}">${text}</span>`)
    .join('<span class="m-sep">◆</span>');
  const track = `<div class="marquee-group">${group}<span class="m-sep">◆</span></div>`;

  const year = new Date().getFullYear();
  const repo = "https://github.com/DiegoCordova7/morphe-docs";

  container.innerHTML = `
    <div class="site-footer" role="contentinfo">
      <div class="footer-marquee" aria-hidden="true">
        <div class="marquee-track">${track}${track}</div>
      </div>

      <div class="container footer-main">
        <div class="footer-giant" aria-hidden="true">morphe</div>
        <div class="footer-brand">
          <p class="footer-tagline">Give your data a <em>shape</em>.<br />Then let it speak.</p>
          <div class="btn-row">
            <a href="playground.html" class="btn btn-primary">Try the playground</a>
            <a href="manifesto.html" class="btn btn-ghost">Read the manifesto</a>
          </div>
        </div>

        <div class="footer-col">
          <span class="footer-col-title">Explore</span>
          <div class="footer-links">
            <a href="index.html">Home</a>
            <a href="manifesto.html">Manifesto</a>
            <a href="syntax.html">Syntax guide</a>
            <a href="playground.html">Playground</a>
          </div>
        </div>

        <div class="footer-col">
          <span class="footer-col-title">Project</span>
          <div class="footer-links">
            <a href="${repo}" target="_blank">Source on GitHub ↗</a>
            <a href="${repo}/issues" target="_blank">Report an issue ↗</a>
          </div>
        </div>
      </div>

      <div class="container footer-bar">
        <span>© ${year} Morphe — built from scratch, from lexer to VM.</span>
        <button type="button" class="footer-top">↑ Back to top</button>
      </div>

    </div>
  `;

  const footer = container.querySelector(".site-footer");
  const giant = container.querySelector(".footer-giant");

  footer.addEventListener("pointermove", (event) => {
    const rect = giant.getBoundingClientRect();
    giant.style.setProperty("--mx", `${event.clientX - rect.left}px`);
    giant.style.setProperty("--my", `${event.clientY - rect.top}px`);
  });

  container.querySelector(".footer-top").addEventListener("click", () => {
    window.scrollTo({ top: 0, behavior: "smooth" });
  });
}

function setActiveNav() {
  const currentPath = window.location.pathname.split("/").pop() || "index.html";
  const links = document.querySelectorAll(".nav-links a");
  links.forEach(link => {
    const pageAttr = link.getAttribute("data-page");
    if (pageAttr === currentPath) {
      link.classList.add("active");
    } else {
      link.classList.remove("active");
    }
  });
}