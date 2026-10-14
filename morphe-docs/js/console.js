let consoleOutput = [];

export function setConsoleStatus(state, label) {
  const dot = document.getElementById("status-dot");
  const text = document.getElementById("status-text");

  dot.className = "status-dot" + (state ? ` is-${state}` : "");
  text.textContent = label;
}

export function setConsoleMessage(text, state) {
  const consoleEl = document.getElementById("console-output");
  consoleEl.className = "console" + (state ? ` is-${state}` : "");
  consoleEl.textContent = text;
}

export function clearConsoleOutput() {
  consoleOutput = [];
  const consoleEl = document.getElementById("console-output");
  consoleEl.replaceChildren();
}

export function renderConsole() {
  const consoleEl = document.getElementById("console-output");
  consoleEl.replaceChildren();

  for (const line of consoleOutput) {
    const outputLine = document.createElement("div");
    outputLine.className = "console-line";
    outputLine.textContent = line;
    consoleEl.appendChild(outputLine);
  }

  consoleEl.scrollTop = consoleEl.scrollHeight;
}

export function addOutput(output) {
  if (!output || output.length === 0) return;
  consoleOutput.push(...output);
  renderConsole();
}

export function createInputPrompt(onSubmit) {
  const consoleEl = document.getElementById("console-output");

  const promptLine = document.createElement("div");
  promptLine.className = "console-input";

  const prompt = document.createElement("span");
  prompt.textContent = "> ";

  const input = document.createElement("input");
  input.type = "text";
  input.autocomplete = "off";
  input.spellcheck = false;

  promptLine.appendChild(prompt);
  promptLine.appendChild(input);

  consoleEl.appendChild(promptLine);
  consoleEl.scrollTop = consoleEl.scrollHeight;

  input.focus();

  input.addEventListener("keydown", async (event) => {
    if (event.key !== "Enter") return;

    event.preventDefault();
    const value = input.value;

    input.disabled = true;
    promptLine.classList.add("submitted");
    consoleOutput.push(`> ${value}`);
    promptLine.remove();
    renderConsole();

    if (onSubmit) {
      await onSubmit(value);
    }
  });
}
