from xeger import Xeger
from random import random, randint, seed
from sympy import expand
import re
import subprocess

location = '../War_of_Holy_Grail/'
command = 'java -jar Alterego.jar'
test_time = 1000
seed(random())


def sign():
    return Xeger().xeger(r'[+-]')


def white_term(limit):
    return Xeger(limit).xeger('[ \t]*')


def signed_integer(limit):
    return Xeger(limit).xeger(r'[+-]?[0-9]+')


def exponent():
    return '**' + white_term(2) + Xeger().xeger(r'\+?[0-]')


def constant_factor():
    return signed_integer(5)


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


def expression():
    expr = Xeger().xeger(r'[+-]?')
    for i in range(randint(1, 5)):
        expr += sign() if i > 0 else ''
        expr += term()
    return expr


def delete_leading_zero(expr):
    expr = ' ' + expr
    pattern = re.compile(r'[ \t()+*-](0+)[1-9]')
    while True:
        matcher = pattern.search(expr)
        if matcher:
            start, end = matcher.span(1)
            expr = expr[:start] + expr[end:]
        else:
            break
    return expr


def test_single(self):
    self_for_python = delete_leading_zero(self)
    self_for_java = self
    print('expr for java  :', self_for_java)
    print('expr for python:', self_for_python)

    _sp = subprocess.Popen(command, cwd=location, stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    _stdout, _stderr = _sp.communicate(input=str.encode(self_for_java))

    self_back_from_java = bytes.decode(_stdout).replace('Mode: Normal', '').strip()

    self_from_python = expand(self_for_python)
    self_from_java = expand(self_back_from_java)
    print('[  get ]:', self_from_java)
    print('[expect]:', self_from_python)
    if self_from_java == self_from_python:
        print('== correct! ==')
    else:
        print('== wrong! ==')


# # test certain case
# self_expr = '-(x**1+-9318)*(1908)*(x**5+x**3+x**  +1)'
# test_single(self_expr)


# # generate only
# for i in range(test_time):
#     print(expression())


# test batches case
if __name__ == '__main__':
    for i in range(test_time):
        print('# test', i)
        expression_origin = expression()
        expression_for_python = delete_leading_zero(expression_origin)
        expression_for_java = expression_origin

        print('expr for java:', expression_for_java)
        print('expr for python', expression_for_python)

        sp = subprocess.Popen(command, cwd=location, stdin=subprocess.PIPE, stdout=subprocess.PIPE,
                              stderr=subprocess.PIPE)
        stdout, stderr = sp.communicate(input=str.encode(expression_for_java))

        expression_back_from_java = bytes.decode(stdout).replace('Mode: Normal', '').strip()

        ans_from_python = expand(expression_for_python)
        ans_from_java = expand(expression_back_from_java)
        print('[  get ]:', ans_from_java)
        print('[expect]:', ans_from_python)
        if ans_from_java == ans_from_python:
            print('== correct! ==')
        else:
            print('== wrong! ==')
            break

