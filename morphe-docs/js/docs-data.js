const DOCS_SECTIONS = [
  {
    id: "variables",
    title: "Variables",
    file: "variables.mrf",
    snippets: [
      {
        label: "Declaration",
        code: `age <span class="tok-punct">:</span> <span class="tok-type">Integer</span> <span class="tok-op">=</span> 20<span class="tok-punct">;</span>
height <span class="tok-punct">:</span> <span class="tok-type">Double</span> <span class="tok-op">=</span> 1.75<span class="tok-punct">;</span>
name <span class="tok-punct">:</span> <span class="tok-type">String</span> <span class="tok-op">=</span> <span class="tok-str">"Diego"</span><span class="tok-punct">;</span>
active <span class="tok-punct">:</span> <span class="tok-type">Boolean</span> <span class="tok-op">=</span> <span class="tok-kw">true</span><span class="tok-punct">;</span>`,
      },
      {
        label: "Assignment",
        code: `age <span class="tok-op">=</span> 21<span class="tok-punct">;</span>
name <span class="tok-op">=</span> <span class="tok-str">"Moctezuma"</span><span class="tok-punct">;</span>`,
      },
      {
        label: "Compound assignment",
        code: `age <span class="tok-op">+=</span> 1<span class="tok-punct">;</span>
age <span class="tok-op">-=</span> 2<span class="tok-punct">;</span>
age <span class="tok-op">*=</span> 2<span class="tok-punct">;</span>
age <span class="tok-op">/=</span> 2<span class="tok-punct">;</span>
age <span class="tok-op">%=</span> 2<span class="tok-punct">;</span>`,
      },
      {
        label: "Increment / decrement",
        code: `age<span class="tok-op">++</span><span class="tok-punct">;</span>
age<span class="tok-op">--</span><span class="tok-punct">;</span>`,
      },
    ],
  },
  {
    id: "constants",
    title: "Constants",
    file: "constants.mrf",
    snippets: [
      {
        code: `<span class="tok-fn">MAX_AGE</span> <span class="tok-punct">:</span> <span class="tok-punct">#</span><span class="tok-type">Integer</span> <span class="tok-op">=</span> 100<span class="tok-punct">;</span>
<span class="tok-fn">LANGUAGE</span> <span class="tok-punct">:</span> <span class="tok-punct">#</span><span class="tok-type">String</span> <span class="tok-op">=</span> <span class="tok-str">"Morphe"</span><span class="tok-punct">;</span>`,
      },
      {
        note: "Declared using<code>name: #Type</code>.Constants cannot be reassigned after their initial value.",
      },
    ],
  },
  {
    id: "types",
    title: "Types",
    file: "types.mrf",
    snippets: [
      {
        label: "Primitive types",
        code: `<span class="tok-type">Integer</span>
<span class="tok-type">Double</span>
<span class="tok-type">Boolean</span>
<span class="tok-type">String</span>`,
      },
      {
        label: "Void & null",
        note: "<code>Void</code> represents the absence of a return value. <code>null</code> represents the absence of a value.",
      },
      {
        label: "Composite types",
        code: `<span class="tok-kw">type</span> <span class="tok-type">Name</span> <span class="tok-punct">{</span>
    name <span class="tok-punct">:</span> <span class="tok-type">String</span><span class="tok-punct">;</span>
<span class="tok-punct">}</span>`,
        note: 'See <a href="#composite-types">Composite types</a> below for creation and field access.',
      },
      {
        label: "Anonymous composite types",
        code: `<span class="tok-punct">{</span> name <span class="tok-punct">:</span> <span class="tok-type">String</span><span class="tok-punct">,</span> surface <span class="tok-punct">:</span> <span class="tok-type">Double</span> <span class="tok-punct">}</span>`,
        note: "Anonymous composite types use structural compatibility, allowing named composite instances to be assigned where matching fields are required.",
      },
      {
        label: "Arrays as a type",
        code: `<span class="tok-type">Integer</span><span class="tok-punct">[]</span>
<span class="tok-type">Integer</span><span class="tok-punct">[][]</span>
<span class="tok-type">String</span><span class="tok-punct">[]</span>
<span class="tok-punct">{</span> name <span class="tok-punct">:</span> <span class="tok-type">String</span> <span class="tok-punct">}</span><span class="tok-punct">[]</span>`,
        note: "Arrays may contain primitive types, named composites, or anonymous composite types.",
      },
    ],
  },
  {
    id: "arrays",
    title: "Arrays",
    file: "arrays.mrf",
    snippets: [
      {
        label: "Declaration",
        code: `numbers <span class="tok-punct">:</span> <span class="tok-type">Integer</span><span class="tok-punct">[]</span> <span class="tok-op">=</span> <span class="tok-kw">new</span> <span class="tok-type">Integer</span><span class="tok-punct">[</span>10<span class="tok-punct">]</span><span class="tok-punct">;</span>
names <span class="tok-punct">:</span> <span class="tok-type">String</span><span class="tok-punct">[]</span> <span class="tok-op">=</span> <span class="tok-kw">new</span> <span class="tok-type">String</span><span class="tok-punct">[</span>5<span class="tok-punct">]</span><span class="tok-punct">;</span>
locations <span class="tok-punct">:</span> <span class="tok-punct">{</span> name <span class="tok-punct">:</span> <span class="tok-type">String</span> <span class="tok-punct">}</span><span class="tok-punct">[]</span> <span class="tok-op">=</span> <span class="tok-kw">new</span> <span class="tok-punct">{</span> name <span class="tok-punct">:</span> <span class="tok-type">String</span> <span class="tok-punct">}</span><span class="tok-punct">[</span>1<span class="tok-punct">]</span><span class="tok-punct">;</span>`,
      },
      {
        label: "Multidimensional",
        code: `matrix <span class="tok-punct">:</span> <span class="tok-type">Integer</span><span class="tok-punct">[][]</span> <span class="tok-op">=</span> <span class="tok-kw">new</span> <span class="tok-type">Integer</span><span class="tok-punct">[</span>3<span class="tok-punct">]</span><span class="tok-punct">[</span>4<span class="tok-punct">]</span><span class="tok-punct">;</span>`,
      },
      {
        label: "Access & assignment",
        code: `numbers<span class="tok-punct">[</span>0<span class="tok-punct">]</span><span class="tok-punct">;</span>
matrix<span class="tok-punct">[</span>1<span class="tok-punct">]</span><span class="tok-punct">[</span>2<span class="tok-punct">]</span><span class="tok-punct">;</span>
numbers<span class="tok-punct">[</span>0<span class="tok-punct">]</span> <span class="tok-op">=</span> 42<span class="tok-punct">;</span>
matrix<span class="tok-punct">[</span>1<span class="tok-punct">]</span><span class="tok-punct">[</span>2<span class="tok-punct">]</span> <span class="tok-op">=</span> 10<span class="tok-punct">;</span>`,
      },
      {
        label: "Length",
        code: `size <span class="tok-punct">:</span> <span class="tok-type">Integer</span> <span class="tok-op">=</span> numbers<span class="tok-punct">.</span>length<span class="tok-punct">;</span>`,
      },
    ],
  },
  {
    id: "operators",
    title: "Operators",
    file: "operators.mrf",
    snippets: [
      {
        label: "Arithmetic",
        code: `a <span class="tok-op">+</span> b<span class="tok-punct">;</span>   <span class="tok-com">// addition</span>
a <span class="tok-op">-</span> b<span class="tok-punct">;</span>   <span class="tok-com">// subtraction</span>
a <span class="tok-op">*</span> b<span class="tok-punct">;</span>   <span class="tok-com">// multiplication</span>
a <span class="tok-op">/</span> b<span class="tok-punct">;</span>   <span class="tok-com">// division</span>
a <span class="tok-op">%</span> b<span class="tok-punct">;</span>   <span class="tok-com">// modulo</span>`,
      },
      {
        label: "Comparison",
        code: `age <span class="tok-op">==</span> 18<span class="tok-punct">;</span>
age <span class="tok-op">!=</span> 18<span class="tok-punct">;</span>
age <span class="tok-op">&lt;</span> 18<span class="tok-punct">;</span>
age <span class="tok-op">&lt;=</span> 18<span class="tok-punct">;</span>
age <span class="tok-op">&gt;</span> 18<span class="tok-punct">;</span>
age <span class="tok-op">&gt;=</span> 18<span class="tok-punct">;</span>`,
      },
      {
        label: "Structural match / compatibility",
        code: `city1 <span class="tok-op">~=</span> city2<span class="tok-punct">;</span> <span class="tok-com">// structural match</span>`,
      },
      {
        label: "Unary & update",
        code: `<span class="tok-op">-</span>value<span class="tok-punct">;</span>
<span class="tok-op">+</span>value<span class="tok-punct">;</span>
identifier<span class="tok-op">++</span><span class="tok-punct">;</span>
identifier<span class="tok-op">--</span><span class="tok-punct">;</span>`,
      },
    ],
  },
  {
    id: "control-flow",
    title: "Control flow",
    file: "control_flow.mrf",
    snippets: [
      {
        label: "If / else",
        code: `<span class="tok-kw">if</span> <span class="tok-punct">(</span>condition<span class="tok-punct">)</span> <span class="tok-punct">{</span>
    statements
<span class="tok-punct">}</span> <span class="tok-kw">else</span> <span class="tok-punct">{</span>
    statements
<span class="tok-punct">}</span>`,
      },
      {
        label: "If expression",
        code: `<span class="tok-kw">if</span> <span class="tok-punct">(</span>condition<span class="tok-punct">)</span> expression <span class="tok-kw">else</span> expression<span class="tok-punct">;</span>`,
      },
      {
        label: "While / do-while",
        code: `<span class="tok-kw">while</span> <span class="tok-punct">(</span>condition<span class="tok-punct">)</span> <span class="tok-punct">{</span>
    statements
<span class="tok-punct">}</span>

<span class="tok-kw">do</span> <span class="tok-punct">{</span>
    statements
<span class="tok-punct">}</span> <span class="tok-kw">while</span> <span class="tok-punct">(</span>condition<span class="tok-punct">)</span><span class="tok-punct">;</span>`,
      },
      {
        label: "For",
        code: `<span class="tok-kw">for</span> <span class="tok-punct">(</span>i <span class="tok-punct">:</span> <span class="tok-type">Integer</span> <span class="tok-op">=</span> 0<span class="tok-punct">;</span> i <span class="tok-op">&lt;</span> 10<span class="tok-punct">;</span> i<span class="tok-op">++</span><span class="tok-punct">)</span> <span class="tok-punct">{</span>
    <span class="tok-fn">print</span><span class="tok-punct">(</span>i<span class="tok-punct">)</span><span class="tok-punct">;</span>
<span class="tok-punct">}</span>`,
      },
    ],
  },
  {
    id: "functions",
    title: "Functions",
    file: "functions.mrf",
    snippets: [
      {
        label: "Declaration & return",
        code: `<span class="tok-kw">func</span> <span class="tok-fn">add</span><span class="tok-punct">(</span>a <span class="tok-punct">:</span> <span class="tok-type">Integer</span><span class="tok-punct">,</span> b <span class="tok-punct">:</span> <span class="tok-type">Integer</span><span class="tok-punct">)</span> <span class="tok-punct">:</span> <span class="tok-type">Integer</span> <span class="tok-punct">{</span>
    <span class="tok-kw">return</span> a <span class="tok-op">+</span> b<span class="tok-punct">;</span>
<span class="tok-punct">}</span>`,
      },
      {
        label: "Composites & anonymous types in functions",
        code: `<span class="tok-kw">type</span> <span class="tok-type">City</span> <span class="tok-punct">{</span>
    name <span class="tok-punct">:</span> <span class="tok-type">String</span><span class="tok-punct">;</span>
    surface <span class="tok-punct">:</span> <span class="tok-type">Double</span><span class="tok-punct">;</span>
<span class="tok-punct">}</span>

<span class="tok-kw">func</span> <span class="tok-fn">printCityName</span><span class="tok-punct">(</span>loc <span class="tok-punct">:</span> <span class="tok-punct">{</span> name <span class="tok-punct">:</span> <span class="tok-type">String</span> <span class="tok-punct">}</span><span class="tok-punct">)</span> <span class="tok-punct">:</span> <span class="tok-type">Void</span> <span class="tok-punct">{</span>
    <span class="tok-fn">print</span><span class="tok-punct">(</span>loc<span class="tok-punct">.</span>name<span class="tok-punct">)</span><span class="tok-punct">;</span>
<span class="tok-punct">}</span>`,
        note: "Functions can accept named composites, anonymous composite types, or return composite instances seamlessly using structural compatibility.",
      },
      {
        label: "Invocation",
        code: `<span class="tok-fn">add</span><span class="tok-punct">(</span>10<span class="tok-punct">,</span> 20<span class="tok-punct">)</span><span class="tok-punct">;</span>
c <span class="tok-punct">:</span> <span class="tok-type">City</span> <span class="tok-op">=</span> <span class="tok-kw">new</span> <span class="tok-type">City</span> <span class="tok-punct">{</span> name <span class="tok-op">=</span> <span class="tok-str">"Moctezuma"</span><span class="tok-punct">;</span> surface <span class="tok-op">=</span> 1763.39<span class="tok-punct">;</span> <span class="tok-punct">}</span><span class="tok-punct">;</span>
<span class="tok-fn">printCityName</span><span class="tok-punct">(</span>c<span class="tok-punct">)</span><span class="tok-punct">;</span>`,
      },
    ],
  },
  {
    id: "match",
    title: "Match",
    file: "match.mrf",
    snippets: [
      {
        label: "Literal & wildcard patterns",
        code: `<span class="tok-kw">match</span> <span class="tok-punct">(</span>value<span class="tok-punct">)</span> <span class="tok-punct">{</span>
    0 <span class="tok-op">=&gt;</span> <span class="tok-punct">{</span>
        <span class="tok-fn">print</span><span class="tok-punct">(</span><span class="tok-str">"Zero"</span><span class="tok-punct">)</span><span class="tok-punct">;</span>
    <span class="tok-punct">}</span>

    1 <span class="tok-op">=&gt;</span> <span class="tok-punct">{</span>
        <span class="tok-fn">print</span><span class="tok-punct">(</span><span class="tok-str">"One"</span><span class="tok-punct">)</span><span class="tok-punct">;</span>
    <span class="tok-punct">}</span>

    _ <span class="tok-op">=&gt;</span> <span class="tok-punct">{</span>
        <span class="tok-fn">print</span><span class="tok-punct">(</span><span class="tok-str">"Other"</span><span class="tok-punct">)</span><span class="tok-punct">;</span>
    <span class="tok-punct">}</span>
<span class="tok-punct">}</span>`,
      },
      {
        label: "Or pattern",
        code: `<span class="tok-kw">match</span> <span class="tok-punct">(</span>value<span class="tok-punct">)</span> <span class="tok-punct">{</span>
    1 <span class="tok-op">|</span> 2 <span class="tok-op">|</span> 3 <span class="tok-op">=&gt;</span> <span class="tok-punct">{</span>
        <span class="tok-fn">print</span><span class="tok-punct">(</span><span class="tok-str">"Small"</span><span class="tok-punct">)</span><span class="tok-punct">;</span>
    <span class="tok-punct">}</span>

    _ <span class="tok-op">=&gt;</span> <span class="tok-punct">{</span>
        <span class="tok-fn">print</span><span class="tok-punct">(</span><span class="tok-str">"Other"</span><span class="tok-punct">)</span><span class="tok-punct">;</span>
    <span class="tok-punct">}</span>
<span class="tok-punct">}</span>`,
      },
      {
        label: "Range pattern",
        code: `<span class="tok-kw">match</span> <span class="tok-punct">(</span>age<span class="tok-punct">)</span> <span class="tok-punct">{</span>
    0 <span class="tok-op">..</span> 17 <span class="tok-op">=&gt;</span> <span class="tok-punct">{</span>
        <span class="tok-fn">print</span><span class="tok-punct">(</span><span class="tok-str">"Minor"</span><span class="tok-punct">)</span><span class="tok-punct">;</span>
    <span class="tok-punct">}</span>

    18 <span class="tok-op">..</span> 64 <span class="tok-op">=&gt;</span> <span class="tok-punct">{</span>
        <span class="tok-fn">print</span><span class="tok-punct">(</span><span class="tok-str">"Adult"</span><span class="tok-punct">)</span><span class="tok-punct">;</span>
    <span class="tok-punct">}</span>

    _ <span class="tok-op">=&gt;</span> <span class="tok-punct">{</span>
        <span class="tok-fn">print</span><span class="tok-punct">(</span><span class="tok-str">"Senior"</span><span class="tok-punct">)</span><span class="tok-punct">;</span>
    <span class="tok-punct">}</span>
<span class="tok-punct">}</span>`,
      },
      {
        label: "Composite pattern",
        code: `<span class="tok-kw">match</span> <span class="tok-punct">(</span>city<span class="tok-punct">)</span> <span class="tok-punct">{</span>
    <span class="tok-type">City</span> <span class="tok-punct">{</span> name<span class="tok-punct">:</span> <span class="tok-str">"Moctezuma"</span> <span class="tok-punct">}</span> <span class="tok-op">=&gt;</span> <span class="tok-punct">{</span>
        <span class="tok-fn">print</span><span class="tok-punct">(</span>city<span class="tok-punct">.</span>name<span class="tok-punct">)</span><span class="tok-punct">;</span>
    <span class="tok-punct">}</span>
<span class="tok-punct">}</span>`,
        note: "Nested patterns may be used for individual fields inside a composite pattern.",
      },
      {
        label: "Variable pattern",
        code: `target <span class="tok-punct">:</span> <span class="tok-type">Integer</span> <span class="tok-op">=</span> 5<span class="tok-punct">;</span>
<span class="tok-kw">match</span> <span class="tok-punct">(</span>value<span class="tok-punct">)</span> <span class="tok-punct">{</span>
    target <span class="tok-op">=&gt;</span> <span class="tok-punct">{</span>
        <span class="tok-fn">print</span><span class="tok-punct">(</span><span class="tok-str">"Matches target"</span><span class="tok-punct">)</span><span class="tok-punct">;</span>
    <span class="tok-punct">}</span>

    _ <span class="tok-op">=&gt;</span> <span class="tok-punct">{</span>
        <span class="tok-fn">print</span><span class="tok-punct">(</span><span class="tok-str">"No match"</span><span class="tok-punct">)</span><span class="tok-punct">;</span>
    <span class="tok-punct">}</span>
<span class="tok-punct">}</span>`,
        note: "A variable pattern matches against that variable's current value — it's a comparison, not a binding. No new variable is captured from the match.",
      },
      {
        label: "Statement vs. expression",
        code: `texto <span class="tok-punct">:</span> <span class="tok-type">String</span> <span class="tok-op">=</span> <span class="tok-kw">match</span> <span class="tok-punct">(</span>a<span class="tok-punct">)</span> <span class="tok-punct">{</span>
    1 <span class="tok-op">=&gt;</span> <span class="tok-str">"uno"</span><span class="tok-punct">;</span>
    2 <span class="tok-op">=&gt;</span> <span class="tok-str">"dos"</span><span class="tok-punct">;</span>
    _ <span class="tok-op">=&gt;</span> <span class="tok-str">"otro"</span><span class="tok-punct">;</span>
<span class="tok-punct">}</span><span class="tok-punct">;</span>`,
        note: "As a statement (the examples above), each case uses <code>{}</code> for side effects. As an expression, there's no <code>{}</code> — each case is the value directly, separated by <code>;</code>, and the whole <code>match</code> ends in <code>;</code> like any other value assigned to a variable.",
      },
    ],
  },
  {
    id: "error-handling",
    title: "Attempt / error handling",
    file: "errors.mrf",
    snippets: [
      {
        label: "Attempt block",
        code: `r <span class="tok-punct">:</span> <span class="tok-type">Integer</span> <span class="tok-op">=</span> <span class="tok-kw">attempt</span> <span class="tok-punct">{</span>
    10 <span class="tok-op">/</span> x<span class="tok-punct">;</span>
<span class="tok-punct">}</span> <span class="tok-op">=&gt;</span> <span class="tok-op">-</span>1<span class="tok-punct">;</span>`,
        note: "The block's last expression is the value returned when nothing fails. <code>=&gt; fallback</code> is required and must match the variable's type.",
      },
      {
        label: "With onError",
        code: `r <span class="tok-punct">:</span> <span class="tok-type">Integer</span> <span class="tok-op">=</span> <span class="tok-kw">attempt</span> <span class="tok-punct">{</span>
    a <span class="tok-punct">:</span> <span class="tok-type">Integer</span> <span class="tok-op">=</span> 10<span class="tok-punct">;</span>
    b <span class="tok-punct">:</span> <span class="tok-type">Integer</span> <span class="tok-op">=</span> 0<span class="tok-punct">;</span>
    a <span class="tok-op">/</span> b<span class="tok-punct">;</span>
<span class="tok-punct">}</span> <span class="tok-op">=&gt;</span> <span class="tok-op">-</span>1 <span class="tok-kw">onError</span><span class="tok-punct">(</span>e<span class="tok-punct">)</span> <span class="tok-op">=&gt;</span> <span class="tok-fn">print</span><span class="tok-punct">(</span><span class="tok-str">"Division by zero"</span><span class="tok-punct">)</span><span class="tok-punct">;</span>`,
        note: '<code>onError(e)</code> is optional and only runs a side-effect statement — it does not change the returned value, which always comes from <code>=&gt; fallback</code> when there\'s an error. <code>e</code> is bound to whichever error kind occurred (one of <code>DivisionByZero</code>, <code>ModuloByZero</code>, <code>InvalidArrayIndex</code>). Here <code>r</code> ends up <code>-1</code> and <code>"Division by zero"</code> gets printed.',
      },
      {
        label: "Supported error kinds",
        code: `<span class="tok-type">DivisionByZero</span>
<span class="tok-type">ModuloByZero</span>
<span class="tok-type">InvalidArrayIndex</span>`,
      },
    ],
  },
  {
    id: "composite-types",
    title: "Composite types",
    file: "composite_types.mrf",
    snippets: [
      {
        label: "Declaration",
        code: `<span class="tok-kw">type</span> <span class="tok-type">Person</span> <span class="tok-punct">{</span>
    name <span class="tok-punct">:</span> <span class="tok-type">String</span><span class="tok-punct">;</span>
    age <span class="tok-punct">:</span> <span class="tok-type">Integer</span><span class="tok-punct">;</span>
<span class="tok-punct">}</span>`,
      },
      {
        label: "Creation",
        code: `<span class="tok-kw">new</span> <span class="tok-type">Person</span> <span class="tok-punct">{</span>
    name <span class="tok-op">=</span> <span class="tok-str">"Diego"</span><span class="tok-punct">;</span>
    age <span class="tok-op">=</span> 20<span class="tok-punct">;</span>
<span class="tok-punct">}</span>`,
      },
      {
        label: "Field access & assignment",
        code: `person<span class="tok-punct">.</span>name<span class="tok-punct">;</span>
person<span class="tok-punct">.</span>age<span class="tok-punct">;</span>
person<span class="tok-punct">.</span>age <span class="tok-op">=</span> 21<span class="tok-punct">;</span>
person<span class="tok-punct">.</span>age <span class="tok-op">+=</span> 1<span class="tok-punct">;</span>`,
      },
    ],
  },
  {
    id: "io",
    title: "I/O",
    file: "io.mrf",
    snippets: [
      {
        label: "Print",
        code: `<span class="tok-fn">print</span><span class="tok-punct">(</span><span class="tok-str">"Hello, Morphe!"</span><span class="tok-punct">)</span><span class="tok-punct">;</span>
<span class="tok-fn">print</span><span class="tok-punct">(</span>age<span class="tok-punct">)</span><span class="tok-punct">;</span>
<span class="tok-fn">print</span><span class="tok-punct">(</span>person<span class="tok-punct">.</span>name<span class="tok-punct">)</span><span class="tok-punct">;</span>
<span class="tok-fn">print</span><span class="tok-punct">(</span>numbers<span class="tok-punct">[</span>0<span class="tok-punct">]</span><span class="tok-punct">;</span>`,
      },
      {
        label: "Read input",
        code: `s <span class="tok-punct">:</span> <span class="tok-type">String</span> <span class="tok-op">=</span> <span class="tok-fn">readString</span><span class="tok-punct">(</span><span class="tok-punct">)</span><span class="tok-punct">;</span>
i <span class="tok-punct">:</span> <span class="tok-type">Integer</span> <span class="tok-op">=</span> <span class="tok-fn">readInteger</span><span class="tok-punct">(</span><span class="tok-punct">)</span><span class="tok-punct">;</span>
b <span class="tok-punct">:</span> <span class="tok-type">Boolean</span> <span class="tok-op">=</span> <span class="tok-fn">readBoolean</span><span class="tok-punct">(</span><span class="tok-punct">)</span><span class="tok-punct">;</span>
d <span class="tok-punct">:</span> <span class="tok-type">Double</span> <span class="tok-op">=</span> <span class="tok-fn">readDouble</span><span class="tok-punct">(</span><span class="tok-punct">)</span><span class="tok-punct">;</span>`,
        note: "One typed read function per primitive type instead of a single generic <code>input()</code>.",
      },
    ],
  },
  {
    id: "literals",
    title: "Literals",
    file: "literals.mrf",
    snippets: [
      {
        code: `42               <span class="tok-com">// Integer</span>
<span class="tok-op">-</span>10              <span class="tok-com">// Integer</span>
3.14             <span class="tok-com">// Double</span>
<span class="tok-op">-</span>0.5             <span class="tok-com">// Double</span>
<span class="tok-kw">true</span>             <span class="tok-com">// Boolean</span>
<span class="tok-kw">false</span>            <span class="tok-com">// Boolean</span>
<span class="tok-str">"Hello, Morphe!"</span> <span class="tok-com">// String</span>
<span class="tok-kw">null</span>             <span class="tok-com">// Null</span>`,
      },
    ],
  },
];