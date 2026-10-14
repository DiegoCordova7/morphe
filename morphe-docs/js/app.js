import { runCodeApi, sendInputApi } from "./api.js";
import { initEditor, getEditorValue, loadSampleCode } from "./editor.js";
import {
  setConsoleStatus,
  setConsoleMessage,
  clearConsoleOutput,
  addOutput,
  createInputPrompt,
} from "./console.js";

let currentSessionId = null;
let waitingForInput = false;

function handleExecutionResponse(data) {
  if (!data.success) {
    currentSessionId = null;
    waitingForInput = false;
    setConsoleStatus("error", "Failed");
    clearConsoleOutput();

    if (data.output && data.output.length > 0) {
      addOutput(data.output);
    }

    if (data.error) {
      const consoleEl = document.getElementById("console-output");
      const errLine = document.createElement("div");
      errLine.className = "console-line is-error";
      errLine.textContent = data.error;
      consoleEl.appendChild(errLine);
    }

    document.getElementById("run-button").disabled = false;
    return;
  }

  if (data.output && data.output.length > 0) {
    addOutput(data.output);
  }

  if (data.state === "WAITING_FOR_INPUT") {
    currentSessionId = data.sessionId;
    waitingForInput = true;
    setConsoleStatus("waiting", "Waiting for input…");

    createInputPrompt(async (value) => {
      waitingForInput = false;
      await provideInput(value);
    });
    return;
  }

  if (data.state === "HALTED") {
    currentSessionId = null;
    waitingForInput = false;
    setConsoleStatus("success", "Success");
    document.getElementById("run-button").disabled = false;
  }
}

async function runCode() {
  const code = getEditorValue();
  const runButton = document.getElementById("run-button");

  runButton.disabled = true;
  currentSessionId = null;
  waitingForInput = false;

  clearConsoleOutput();
  setConsoleStatus("running", "Running…");
  setConsoleMessage("Compiling and running against the Morphe API…", null);

  try {
    const data = await runCodeApi(code);
    handleExecutionResponse(data);
  } catch (err) {
    currentSessionId = null;
    waitingForInput = false;
    setConsoleStatus("error", "Connection error");
    setConsoleMessage(
      "Could not reach the Morphe API. Is it running and reachable?\n" +
      err.message,
      "error",
    );
    runButton.disabled = false;
  }
}

async function provideInput(input) {
  if (!currentSessionId) return;

  const sessionId = currentSessionId;
  try {
    const data = await sendInputApi(sessionId, input);
    handleExecutionResponse(data);
  } catch (err) {
    currentSessionId = null;
    waitingForInput = false;
    setConsoleStatus("error", "Connection error");

    const consoleEl = document.getElementById("console-output");
    const errorLine = document.createElement("div");
    errorLine.className = "console-line is-error";
    errorLine.textContent =
      "Could not send input to the Morphe API.\n" + err.message;
    consoleEl.appendChild(errorLine);
    consoleEl.scrollTop = consoleEl.scrollHeight;

    document.getElementById("run-button").disabled = false;
  }
}

function clearConsole() {
  currentSessionId = null;
  waitingForInput = false;
  clearConsoleOutput();
  setConsoleStatus(null, "Idle");
  document.getElementById("run-button").disabled = false;
}

window.addEventListener("DOMContentLoaded", () => {
  initEditor();

  document.getElementById("run-button").addEventListener("click", runCode);

  document.addEventListener("keydown", (event) => {
    if ((event.ctrlKey || event.metaKey) && event.key === "Enter") {
      const runButton = document.getElementById("run-button");
      if (runButton.disabled) return;
      event.preventDefault();
      runCode();
    }
  });
  document
    .getElementById("clear-button")
    .addEventListener("click", clearConsole);
  document
    .getElementById("sample-button")
    .addEventListener("click", loadSampleCode);
});
