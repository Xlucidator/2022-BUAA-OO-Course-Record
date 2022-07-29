# 单数据AOE群体Hack程序-Python

由于一般没有精力和时间读完所有人的代码，因而盲hack也是常选项。但一个数据点重复跑6-7个人实在浪费时间，所以想写一个一次跑多人的程序

### 基本想法
subprocess调用jar包；根据程序第一行判断执行模式；依据模式分别处理得到输出

### 具体实现
```python
import subprocess

location = '../Unit1_Homework2/'
# 待测jar包位置（存放各职阶程序的jar包），记得更改

Name = ['Rider', 'Assassin', 'Berserker', 'Lancer', 'Archer', 'Caster', 'Assassin']  # 'Saber',
# 待测职阶

command = [f'java -jar {name}.jar' for name in Name]
# 生成命令

input_for_java = '''\
2
f(x,y)=x+y
g(x,y)=x*y
x*f(sin(x),cos(x))+g(sin(x),cos(x))\
'''
# 待测数据

for cmd in command:
    sp = subprocess.Popen(cmd, cwd=location, stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    mode = sp.stdout.readline().decode()[6:].strip()       # 获取执行模式
    out = ''    # 待测程序标准输出
    err = ''    # 待测程序标准错误

    if mode == 'Normal':
        normal_input = input_for_java
        out, err = sp.communicate(input=str.encode(normal_input))
    elif mode == 'Parsed':
        trans = subprocess.Popen('./parser_student_win64.exe', stdin=subprocess.PIPE, stdout=subprocess.PIPE,
                                 stderr=subprocess.PIPE)
        exe_out, exe_err = trans.communicate(input=str.encode(input_for_java))
        # 调用官方提供的转化程序（放在同文件夹比较方便，不然再加一下cwd参数），获得符合parsed模式的输入

        parsed_input = exe_out.decode().replace('\t', ' ')  # 似乎单一表达式生成的输入之间会有'\t'，换成space
        out, err = sp.communicate(input=str.encode(parsed_input))
    else:
        print('Unknown')

    out_info_from_java = out.decode().replace('\n', '')
    err_info_from_java = err.decode()
    sp.kill()
    print('\033[32m' + cmd[10:] + ':' + '\033[0m')
    print('\t' + mode)
    print('\t' + '[out]: ' + out_info_from_java)
    if err_info_from_java != '':    # 若待测程序运行时异常，输出异常信息
        print('\t' + '[err]: ' + err_info_from_java)
```

### 疑惑与改进方向
1. 进一步应该支持文件输入数据/随机数据；多数据执行
2. 进一步应简化生成jar包这一过程
3. 与子进程交互时，交替用`stdin.write()`和`stdout.read()`似乎无法完成数据读取；不求甚解所以换成了`readline()`和`communicate()`了
4. 不清楚parsed模式下，标签操作数之间到底是空格还是`\t`，这里直接统一换成空格了
5. 读取返回数据的每一行其实以`\r\n`结尾，不过对运行没影响也就没管