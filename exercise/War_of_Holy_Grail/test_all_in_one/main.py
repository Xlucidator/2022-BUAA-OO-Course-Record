import subprocess
import generate_data as data

location = '../Unit1_Homework3/'
# 待测jar包位置（存放各职阶程序的jar包）

Name = ['Rider', 'Assassin', 'Berserker', 'Saber', 'Archer', 'Caster', 'Lancer1']  # 'Lancer',
# 待测职阶

command = [f'java -jar {name}.jar' for name in Name]
# 生成命令

auto_for_java = '0\n' + data.generate()
self_for_java = '0\nsum(i,0,2,(2*i**2))'
input_for_java = self_for_java if self_for_java != '' else auto_for_java
print('\033[31m' + '[input]\n' + input_for_java + '\033[0m')
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
    print('\t' + '[out]: ' + out_info_from_java)
    if err_info_from_java != '':    # 若待测程序运行时异常，输出异常信息
        print('\t' + '[err]: ' + err_info_from_java)
