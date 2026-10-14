function renderDocs() {
  const toc = document.querySelector(".docs-toc");
  const content = document.querySelector(".docs-content");
  if (!toc || !content || typeof DOCS_SECTIONS === "undefined") return;

  const tocTitle = document.createElement("span");
  tocTitle.className = "docs-toc-title";
  tocTitle.textContent = "On this page";
  toc.appendChild(tocTitle);

  DOCS_SECTIONS.forEach((section) => {
    const link = document.createElement("a");
    link.href = `#${section.id}`;
    link.textContent = section.title;
    toc.appendChild(link);

    const sectionEl = document.createElement("section");
    sectionEl.id = section.id;

    const h2 = document.createElement("h2");
    h2.textContent = section.title;
    sectionEl.appendChild(h2);

    if (section.intro) {
      const introEl = document.createElement("p");
      introEl.innerHTML = section.intro;
      sectionEl.appendChild(introEl);
    }

    const panel = document.createElement("div");
    panel.className = "panel";

    const header = document.createElement("div");
    header.className = "panel-header";
    header.innerHTML = `<span class="filename">${section.file}</span>`;
    panel.appendChild(header);

    const body = document.createElement("div");
    body.className = "panel-body";

    section.snippets.forEach((snippet) => {
      const snippetEl = document.createElement("div");
      snippetEl.className = "snippet";

      if (snippet.label) {
        const labelEl = document.createElement("span");
        labelEl.className = "snippet-label";
        labelEl.textContent = snippet.label;
        snippetEl.appendChild(labelEl);
      }

      if (snippet.code) {
        const pre = document.createElement("pre");
        pre.className = "code-block";
        pre.innerHTML = snippet.code;
        snippetEl.appendChild(pre);
      }

      if (snippet.note) {
        const noteEl = document.createElement("p");
        noteEl.className = "snippet-note";
        noteEl.innerHTML = snippet.note;
        snippetEl.appendChild(noteEl);
      }

      body.appendChild(snippetEl);
    });

    panel.appendChild(body);
    sectionEl.appendChild(panel);
    content.appendChild(sectionEl);
  });
}

function setTocOffset() {
  const nav = document.querySelector("nav");
  const toc = document.querySelector(".docs-toc");
  if (!nav || !toc) return;
  toc.style.top = `${nav.offsetHeight + 24}px`;
}

function setupScrollSpy() {
  const links = document.querySelectorAll(".docs-toc a");
  const sections = document.querySelectorAll(".docs-content section[id]");
  if (!links.length || !sections.length) return;

  const linkForSection = (id) =>
    document.querySelector(`.docs-toc a[href="#${id}"]`);

  const observer = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        if (!entry.isIntersecting) return;
        const link = linkForSection(entry.target.id);
        if (!link) return;
        links.forEach((l) => l.classList.remove("is-active"));
        link.classList.add("is-active");
        link.parentElement.scrollTo({
          left: link.offsetLeft - link.parentElement.clientWidth / 2 + link.clientWidth / 2,
          behavior: "smooth",
        });
      });
    },
    { rootMargin: "-30% 0px -60% 0px", threshold: 0 },
  );

  sections.forEach((section) => observer.observe(section));
}

window.addEventListener("DOMContentLoaded", () => {
  renderDocs();
  setupScrollSpy();
});

function setTocOffset() {
  const nav = document.querySelector("nav");
  const toc = document.querySelector(".docs-toc");
  if (!nav || !toc) return;
  toc.style.top = `${nav.offsetHeight + 24}px`;
}
