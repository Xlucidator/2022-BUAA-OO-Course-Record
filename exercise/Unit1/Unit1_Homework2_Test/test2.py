import random
import subprocess
import sympy

location = ''
command = 'java -jar Caster.jar'

MAX_TERM = 3
MAX_DEPTH = 4
MAX_FACTOR = 3
now_depth = 0

std_java = ""
std_py = ""
fun_java = []  # 随机出了哪些函数，记录一下，java输入时要用到
fun_record = []  # 函数名不能重复，也记录一下

list_sum = ["1", "3", "i", "i+2", "i-1", "sin(i)", "cos(i)", "i*i", "(i-1)*i", "i*i*i", "i*(i+2)+1"]
# fun1,fun2,fun3分别是函数参数为1个，2个，3个的函数
list_fun1 = ["f(x)=x", "f(y)=sin(y)", "f(z)=cos(z)", "f(y)=y*y", "f(x)=2*x+1"]
list_fun2 = ["g(x,y)=x+y", "g(y,z)=sin(y)*cos(z)", "g(x,z)=x*z+x-z", "g(z,y)=y*y-z+2", "g(x,z)=sin(z)*x+z"]
list_fun3 = ["h(x,y,z)=x+y-z", "h(x,z,y)=sin(x)+y+cos(z)", "h(x,z,y)=x+y*(z-1)", "h(x,y,z)=y*y*x*(4-z)", "h(x,y,z)=sin(y)*(x-z)"]
list_fun = [list_fun1, list_fun2, list_fun3]

# 用于替换函数参数的变量，即若函数为f(y)=sin(y)，则从list_var中随机一个当作y填入函数
list_var = ["1", "2", "-1", "x", "x**2", "x**3", "x**0", "2147483649", "1235678909876545678"]


def generate_blank():
    global std_java
    global std_py
    num = random.randint(0, 2)
    for i in range(0, num):
        std_java += " "


def generate_power_or_num():
    global std_java
    global std_py
    if random.randint(0, 1) == 0:  # num
        if random.randint(0, 1) == 0:
            std_java += "-"
            std_py += "-"
        zero = random.randint(0, 2)
        for i in range(0, zero):
            std_java += "0"
        num = random.randint(0, 10)
        std_java += str(num)
        std_py += str(num)
    else:  # pow
        std_java += "x"
        generate_blank()
        std_java += "**"
        generate_blank()
        if random.randint(0, 1) == 0:
            std_java += "+"
        zero = random.randint(0, 2)
        for i in range(0, zero):
            std_java += "0"
        exp = random.randint(0, 3)
        std_java += str(exp)
        std_py += "x**" + str(exp)


def generate_triangle():
    global std_java
    global std_py
    if random.randint(0, 1) == 0:
        std_java += "sin("
        std_py += "sin("
        generate_power_or_num()
        std_java += ")"
        std_py += ")"
    else:
        std_java += "cos("
        std_py += "cos("
        generate_power_or_num()
        std_java += ")"
        std_py += ")"


def generate_sum():
    global std_java
    global std_py
    std_java += "sum(i,"
    down = random.randint(-5, 5)  # 下界
    up = down + random.randint(-1, 3)  # 上界
    std_java += str(down) + "," + str(up) + ","
    num = random.randint(0, len(list_sum) - 1)
    std_java += list_sum[num] + ")"

    if down > up:  # 如果下界大于上界一定要输出0
        std_py += "0"
    else:
        std_py += "("  # 加括号保持优先级
        for i in range(down, up + 1):
            # 将i替换掉，但注意不能把sin中的i替换了，这里的第二个replace就是替换回sin
            std_py += list_sum[num].replace("i", str(i)).replace("s" + str(i) + "n", "sin")
            if i < up:
                std_py += "+"
        std_py += ")"


def generate_function():
    global std_java
    global std_py
    global fun_java
    global fun_record

    p = random.randint(1, len(list_fun1))  # 类型（每个函数列表中的第几个）
    num = random.randint(1, 3)  # 参数个数/函数名
    while num in fun_record:    # 不能重复
        num = random.randint(1, 3)
    fun_record.append(num)      # 记录用过的函数名

    fun_java.append((num, p))   # 记录随机出的函数
    std_java += list_fun[num - 1][p - 1].split("=")[0][0:2]
    # 假如函数为 g(x,y)=x+y ，按等于符号split，第0项是g(x,y)，取 g(

    arr = []  # 记录实际表达式中的参数列表
    for i in range(0, num):
        arr.append(list_var[random.randint(0, len(list_var) - 1)])  # 从参数列表随机
        std_java += arr[i]
        if i < num - 1:
            std_java += ","
    std_java += ")"

    s = list_fun[num - 1][p - 1].split("=")[1]
    # 假如函数为 g(x,y)=x+y ，按等于符号split，第1项是x+y
    for i in range(0, num):
        # 分别将x,y替换为arr中的实际参数
        s = s.replace(list_fun[num - 1][p - 1].split("=")[0][2 + i * 2], arr[i])
    std_py += "(" + s + ")"     # 加括号维持优先级
    # print(list_fun[num - 1][p - 1])
    # print(std_java)
    # print(std_py)


def generate_expression():
    global std_java
    global std_py
    num = random.randint(1, MAX_TERM)
    for i in range(0, num):
        generate_term()
        if i < num - 1:
            if random.randint(0, 1) == 0:
                std_java += "+"
                std_py += "+"
            else:
                std_java += "-"
                std_py += "-"


def generate_term():
    global std_java
    global std_py
    global now_depth
    # 项前的+-
    if random.randint(0, 2) == 0:
        std_java += "+"
    elif random.randint(0, 1) == 0:
        std_java += "-"
        std_py += "-"

    num_factor = random.randint(1, MAX_FACTOR)
    for i in range(0, num_factor):
        if random.randint(0, 5) == 0:
            generate_power_or_num()
        elif random.randint(0, 4) == 0:
            generate_triangle()
        elif random.randint(0, 3) == 0:
            generate_sum()
        elif random.randint(0, 2) == 0 and len(fun_record) < 3:
            generate_function()
        elif now_depth <= MAX_DEPTH:
            now_depth += 1
            std_java += "("
            std_py += "("
            generate_expression()
            std_java += ")"
            std_py += ")"
        else:
            std_java += "1"
            std_py += "1"

        if i < num_factor - 1:
            generate_blank()
            std_java += "*"
            std_py += "*"
            generate_blank()


def execute_java(stdin):
    cmd = ['java', '-jar', 'First.jar']
    proc = subprocess.Popen(command, stdin=subprocess.PIPE,
                            stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    stdout, stderr = proc.communicate(stdin.encode())
    return stdout.decode().split("\n")[1]


if __name__ == '__main__':
    for k in range(1, 201):
        print(str(k) + ":", end="")

        now_depth = 0
        std_java = ""
        std_py = ""
        generate_expression()
        print(std_java)

        # 输出自定义函数
        tmp = str(len(fun_java)) + "\n"
        for j in range(0, len(fun_java)):
            tmp += list_fun[fun_java[j][0] - 1][fun_java[j][1] - 1] + "\n"
        std_java = tmp + std_java

        # 至此已得到std_java, std_py
        # print("java输入：" + std_java)
        # print("python输入：" + std_py)
        # exit(0)

        # global f
        # global difference
        # global f_ans
        # global g_ans

        # try:
        f = execute_java(std_java)
        g = std_py

        # print("java执行结果：\n" + f)

        f_ans = sympy.trigsimp(f)
        g_ans = sympy.trigsimp(g)
        difference = sympy.simplify(sympy.trigsimp(str(f).strip() + "-(" + g + ")"))
        # except Exception as e:
        #     print(e)

        # print("三角函数处理后的java结果：\n" + str(f_ans))
        # print("三角函数处理后的python结果：\n" + str(g_ans))
        # print("差值：\n" + str(difference))

        if difference == 0:
            print("\033[32mAccept!\033[0m")
        else:
            print("\033[31mWrong Answer!\033[0m")
            print("java输入：\n" + std_java)
            print("python输入：\n" + std_py)
            print("java执行结果：\n" + f)
            print("三角函数处理后的java结果：\n" + str(f_ans))
            print("三角函数处理后的python结果：\n" + str(g_ans))
            print("差值：\n" + str(difference))
            break


'''
( ) *  1
+f(x**0)  *()
        (-04 * x ** +1++sum(i,4,7,i*(i+2)+1)) * (sum(i,3,2,i) *x** +000  * sum(i,-2,-3,i*i*i)-(x**3  *1* 1) *1--1 * 1 *1)
        --x  **00*sin(000)
        --x **  01
+x**+3  * 10
++sum(i,2,1,(i-1)*i)* 1 *sin(10)

'''


-sum(i,4,5,i*(i+2)+1)* (x  **+003)
-+x  **2\
+-(sum(i,2,4,i+2)  *(((sum(i,5,7,i*(i+2)+1)*  1  * 1)* 1*x** 2) * sum(i,2,1,i*(i+2)+1)+1 * cos(x** +01) * cos(-000))-+1 * 00  *  1-1*-7)  *1
