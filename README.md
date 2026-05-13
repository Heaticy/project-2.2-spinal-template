# Qin110P

Qin110P is a three-stage pipeline RISC-V processor for ShanghaiTech CS110 Project2.2.

# Initialize
Clone the hardware project `Qin110P`
```
git clone --recursive git@github.com:shanghaitech-cs110-26sp/Qin110P.git Qin110
```

We prepare two software tests. You only have to pass `TestRV32IPart` to complete Project 2.1. However, it is recommended to complete all the instructions in RV32I. If you do so, you can use `TestRV32IFull` to verified your design.

Clone the software project `TestRV32IPart` and `TestRV32IFull`
```
git clone --recursive git@github.com:shanghaitech-cs110-26sp/TestRV32IPart.git TestRV32IPart
git clone --recursive git@github.com:shanghaitech-cs110-26sp/TestRV32IFull.git TestRV32IFull
```

Remember to use `--recursive` beacuse all three projects contain git submodule. You will find some empty submodule folder if you forget to use this option. If this already happend, you can fix this by following commands (e.g. `Qin110P`)
```
cd Qin110P
git submodule init
git submodule update
```

We provide two ways to boot the software tests
1. Use prebuilt binary under `Qin110P/Qin110/prebuilt`.
2. Use binary build from asm in `TestRV32IPart` and `TestRV32Full` by your own.

If you want to build binary by your own, you have to build the riscv toolchain from source code first.

But don't worry, we have prepared everything for you. All you need to do is clone the following repo, create a directory, add it to path, and type `make`. After 5 to 20 minutes, you will have your own riscv toolchain.
```
git clone git@github.com:liyuxuan3003/GCCNone.git
```

Edit `~/.profile` (and also `~/.zprofile`, if you use Zsh instead of Bash), append at the bottom
```
export PATH="$PATH:/opt/riscv-none-elf/bin"
```

Create the directory and change ownership to yourself (use your own username instead of `liyuxuan`)
```
sudo mkdir /opt/riscv-none-elf
sudo chown liyuxuan:liyuxuan /opt/riscv-none-elf
```

Install some necessary dependence to build
```
sudo apt install build-essential libgmp-dev libmpfr-dev libmpc-dev meson ninja-build
```

Finally, type `make` (use `JOBS=` to limit maximum process, it should be lower than cores you have, or your ubuntu might crash during build)
```
make TARGET=riscv-none-elf JOBS=20
```

# File Structure
Suppose all your works are under `~/workspace`, your file structure should be like
```
workspace                        <- Run git clone here
|-- Qin110P
.   |-- Qin110                   <- Run make here
.   .   |-- Makefile
.   .   |-- Qin110Top.scala      <- TOP
.   .   |-- Tests.scala
.   .   |-- Tools.scala
.   .   |-- src                  <- Write all your codes here
.   .   .   |-- Qin110Core.scala
.   .   .   |-- ...
.   .   |-- magicleda       (submodule)
.   .   |-- makefile-spinal (submodule)
.   .   |-- TestRV32Full (-> ../../TestRV32Full/TestRV32Full)
.   .   |-- TestRV32Part (-> ../../TestRV32Part/TestRV32Part)
.   |-- .gitignore
.   |-- .gitmodules 
.   |-- README.md
|-- TestRV32Full
.   |-- TestRV32Full     (<- softlink to here)
.   .   |-- Makefile
.   .   |-- TestRV32Full.s
.   .   |-- makefile-riscv (submodule)
.   |-- .gitignore
.   |-- .gitmodules 
.   |-- README.md
|-- TestRV32Part
.   |-- TestRV32Part     (<- softlink to here)
.   .   |-- Makefile
.   .   |-- TestRV32Part.s
.   .   |-- makefile-riscv (submodule)
.   |-- .gitignore
.   |-- .gitmodules 
.   |-- README.md
```

The `Qin110P` is connected with `TestRV32IPart` and `TestRV32IFull` by softlink, so you have to ensure that all three repos are under the same directory and have correct naming.
- Run `git clone` under same directory.
- The naming of three repos directory have to be `Qin110P`, `TestRV32IPart`, `TestRV32Full`.

If you use VSCode, you should open your workspace exactly on `workspace/Qin110P`. Otherwise, your scala extension can not work correctly.

The design of microarchitecture is free. You can modify definition of io ports and control signal. You can create your own modules or delete existed modules. However, you should not modify any files outside `src`, namely `Qin110Top.scala`, `Tests.scala`, `Tools.scala`. Also, you have to ensure that the io ports of every module used directly in `Qin110Top.scala` unchanged.

# Makefile Usage
Remember that you should run all the following commands under `Qin110P/Qin110`.

Run test `TestRV32IPart`
```
make TestRV32IPart
```

Run test `TestRV32IFull`
```
make TestRV32IFull
```

View the wave of simulation
```
make gtkwave
```

The detailed reports will be generated at `build/TestRV32IPart.txt` and `build/TestRV32IFull.txt`, respectivelly.

In general, you can do such things for any modules
- `make` or `make verilog`: Generate verilog.
- `make sim`: Run the simulation.
- `make gtkwave`: View the wave.

You can use `TARGET` to specify the target module, for example
```
make TARGET=Alu
make sim TARGET=Alu
make gtkwave TARGET=Alu
```

Default value of `TARGET` is `Qin110`. If you want generate verilog for `Qin110`, you have to also specify the program by `ARGS`, as program is the initial value of rom. Default value of `ARGS` is `TestRV32IPart`.
```
make ARGS=TestRV32IPart
make ARGS=TestRV32IFull
```

# VSCode Config
You might want to add these in your `settings.json` (Files > Preferences > Settings > Click "Open Settings (JSON)")
```json
"[scala]": {
    "editor.indentSize": 2,
    "editor.detectIndentation": false,
    "editor.defaultFormatter": "scalameta.metals",
    "editor.suggestSelection": "first",
    "editor.formatOnSave": true,
    "editor.formatOnPaste": true,
},
```

You might want to add these in your `scala.json` (Files > Preferences > Configure Snippets > Enter "scala")
```json
"Spinal Template": {
    "prefix": "@template-spinal",
    "body": [
        "package ${TM_DIRECTORY/.*\\/(.+?)$/$1/}",
        "",
        "import spinal.core._",
        "import spinal.core.sim._",
        "import spinal.lib._",
        "",
        "case class ${TM_FILENAME_BASE}() extends Component {",
        "\tval io = new Bundle {",
        "\t\t$1",
        "\t}",
        "\t$2$0",
        "}",
        "",
        "object ${TM_FILENAME_BASE}Sim extends App {",
        "\tConfig.sim.compile(${TM_FILENAME_BASE}()).doSim { dut =>",
        "\t\tdut.clockDomain.forkStimulus(period = 10, resetCycles = 9)",
        "\t\tdut.clockDomain.waitRisingEdge()",
        "\t\t$3",
        "\t}",
        "}",
        "",
        "object ${TM_FILENAME_BASE}Verilog extends App {",
        "\tConfig.spinal.generateVerilog(${TM_FILENAME_BASE}())",
        "}",
        "",
        "object ${TM_FILENAME_BASE}Vhdl extends App {",
        "\tConfig.spinal.generateVhdl(${TM_FILENAME_BASE}())",
        "}",
        "",
    ]
},
```

In this template, the Makefile is work by assuming that a module named `Alu` should also have three object named `AluSim`, `AluVerilog`, `AluVhdl`. Thus you can use `make sim` and `make` with `TARGET=Alu` to run simulation or generate verilog. By introducing this snippet, if you want to create your own module `MyUnit`, you can simply create a file named `MyUnit.scala` and enter `@template-spinal`. Everything you need will be prepared.
