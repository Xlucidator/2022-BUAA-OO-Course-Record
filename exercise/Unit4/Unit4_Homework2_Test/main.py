from random import random

from generate import *


def main():
    for i in range(random.randint(10, 20)):
        createClass()
    for i in range(random.randint(10, 40)):
        createInterface()
    for i in range(random.randint(10, 100)):
        createAssociationEnd()
    createInterfaceRealization()
    createGeneralization()
    for i in range(random.randint(1, 10)):
        createInteraction()


def data2Stdin():
    random.shuffle(datas)
    for data in datas:
        stdin.write(data + "\n")
    return


def test2Stdin():
    global stdin
    for i in range(300):
        r = random.random()
        if r < 0.5:
            n = random.randint(11, 16)
        else:
            n = random.randint(1, 16)
        # n = random.sample([4,5,9],1)[0]
        # n = 2
        # n = 10
        ret = instrDic[n]()
        stdin.write(ret + '\n')

with open("data.json" , "w") as f:


main()
data2Stdin()
stdin.write("END_OF_MODEL\n")
test2Stdin()

stdin.close()