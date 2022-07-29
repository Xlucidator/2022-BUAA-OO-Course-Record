# 自动化测试方案及实现全流程注意点-Python

其实这种随机没有针对性的测试挺低效，但是搭建自动化测试的过程很有意思，也能学到很多。

整个过程遇到很多问题，所以觉得有记录和提供一些（我认为）较清晰资料的意义。

这是本人第一次从头到尾实现的自动化测试程序，很多函数/模块也是现学现用，写得蠢。望赐教！

## 一、随机生成表达式

#### ·利用xeger模块

xeger模块里就一个Xeger类，构造函数`Xeger(limit=10)`，`limit`参数表示长度限制

还是pypi上写的最简洁清楚：https://pypi.org/project/xeger/

这里使用`.xeger(regex) -> str`方法可以直接根据与正则表达式匹配的字符串。依据形式化表述层层构造，即生成符合规则的表达式。

**例：生成项term**

```python
from xeger import Xeger

def sign():
    return Xeger().xeger(r'[+-]')


def white_term(limit):
    return Xeger(limit).xeger('[ \t]*')


def signed_integer(limit):
    return Xeger(limit).xeger(r'[+-]?[0-9]+')


def exponent():
    return '**' + white_term(2) + Xeger().xeger(r'\+?[0-8]')


def constant_factor():
    return signed_integer(4)


def variable_factor():  # needn't care too much about white_term
    return 'x' + exponent()


def expression_factor():
    expr = ''
    for i in range(randint(1, 4)):
        expr += '+' if i > 0 else ''
        if random() < 0.5:
            expr += constant_factor()
        else:
            expr += variable_factor()
    return '(' + expr + ')'


def term():
    t = ""
    for i in range(randint(1, 4)):
        tmp = random()
        t += '*' if i > 0 else ''
        t += expression_factor() if tmp < 0.4 else (variable_factor() if tmp < 0.8 else constant_factor())
    return Xeger().xeger(r'[+-]?') + t
```

依照此法，我们最终得到函数`def expression()`可满足需求。

#### 问题

- 只保证了幂函数指数不超过8，最终结果大概率超8，不会，感觉处理起来很棘手
- 长度不能保证，空白字符后懒得管了

## 二、python创建子进程调用jar包获取输出

#### ·idea生成jar包

File -> Project Structure ->Artifacts

https://cloud.tencent.com/developer/article/1764737

找了半天资料，感觉这个写的比较清楚。我自己照做的，原理不懂，不知道为MF文件存放目录不能用默认目录，但确实用之后就运行不起来，报错信息忘了

#### ·利用subprocess模块

资料有点杂，可能还是官方文档最清晰。 这个尚可，http://xstarcd.github.io/wiki/Python/python_subprocess_study.html

使用其中的Popen类即可

```python
import subprocess

location = '../Unit1_Homework1/out/artifacts/Unit1_Homework1_jar/'
command = 'java -jar Unit1_Homework1.jar'

sp = subprocess.Popen(command, cwd=location, stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
# 创建管道
# cwd参数：子进程当前目录，也就是jar包所在目录
# stdin, stdout等一定要重定向为subprocess.PIPE，要不然还是在命令行中
# 还有一个shell参数，默认false，改成true的话可以通过shell执行指定指令
stdout, stderr = sp.communicate(input=str.encode(expression_for_java))
# 与子进程交互，向自己的程序发送字符串，接收stdout等返回
expression_back_from_java = bytes.decode(stdout).replace('Mode: Normal', '').strip()
# 坑点1: 交互的信息都是byte型，所以要用decode/encode将str和byte互转
# 坑点2: 官方jar包的输出decode完是'Mode: Normal'和不知名的空白字符（反正'\n'匹配不了），所以干脆用replace().strip()删除多余字符
```

## 三、比较结果判断正误

#### ·利用sympy模块

这篇较为清晰 [https://www.cnblogs.com/zyg123/p/10544277.html#2%E5%B1%95%E5%BC%80-expand](https://www.cnblogs.com/zyg123/p/10544277.html#2展开-expand)

功能十分强大，用`expand()`就好。

思路是，将生成的expression分别喂给python`expand()`和自己的程序，然后比较结果。然而因为各种各样的原因，自己的程序并不会是标准的 多项式降幂排列。 所以我的想法是将自己程序输出的结果交给python再`expand()`。既可以比较，也能间接检测正确性

**warning**：expand()没法解析含前导零的数！

我的想法就是，生成的expression直接喂给自己的程序，然后去除前导零喂给`expand()`函数

```python
expression_origin = expression()

expression_for_python = delete_leading_zero(expression_origin)
expression_for_java = expression_origin
```

#### ·关于去除前导零

可以采用python的正则表达式和捕获组，使用re模块

https://www.runoob.com/python3/python3-reg-expressions.html

和java写法几乎一样

```python
import re

def delete_leading_zero(expr):
    pattern = re.compile(r'[ \t()+*-](0+)[1-9]')
    ...
```

这个表达式还是有问题，若expression以前导零开头就不会被漏过。这样的话，在传入的expr前手动加一个空格应该就好`expr = ' ' + expr`

**获取捕获组位置**：使用`matchObject.span()`

```python
matcher = pattern.search(expr)
start, end = matcher.span(1)    # 1表示捕获组序号
# span()返回捕获的头尾，包前不包后，正好适合切片重组
```

 

批量检测的话，外面套一个for循环就行；一些控制参数也可以提到前面方便统一修改。

最终差不多是这个效果：

![image](http://api.oo.buaa.edu.cn/image/2022_03_05_17_37_18_379edb750269845675111826dcde70ccf3033e97/test_picture.png）