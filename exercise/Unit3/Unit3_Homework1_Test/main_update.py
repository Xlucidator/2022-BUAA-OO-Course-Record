import random
import subprocess

pid = 8
gid = 1


def generateOneInst():
    rate_set = [20, 20, 8, 9, 10, 10, 10, 6, 7]
    global pid
    global gid
    instruct_set = ['ap', 'ar', 'qv', 'qps', 'qci', 'qbs', 'ag', 'atg', 'dfg']
    choose = random.randint(1, 100)
    index = 0
    sum_index = 0
    while sum_index < choose:
        sum_index += rate_set[index]
        index = index + 1
    instruct = instruct_set[index - 1]

    if index == 1:      # ap id name age
        instruct = instruct + ' ' + str(pid) + ' ' + str(random.randint(1, 9999999)) + ' ' + str(
            random.randint(1, 100))
        pid += 1
    elif index == 2:    # ar id1 id2 value
        instruct = instruct + ' ' + str(random.randint(1, pid + 1)) + ' ' + str(
            random.randint(1, pid + 1)) + ' ' + str(
            random.randint(1, 9999999))
    elif index == 3:    # qv id1 id2
        instruct = instruct + ' ' + str(random.randint(1, pid + 1)) + ' ' + str(random.randint(1, pid + 1))
    elif index == 5:    # qci id1 id2
        instruct = instruct + ' ' + str(random.randint(1, pid + 1)) + ' ' + str(random.randint(1, pid + 1))
    elif index == 7:    # ag id
        instruct = instruct + ' ' + str(random.randint(1, gid))
        gid += 1
    elif index == 8:    # atg id1 id2
        instruct = instruct + ' ' + str(random.randint(1, pid + 1)) + ' ' + str(random.randint(1, gid + 1))
    elif index == 9:    # dfg id1 id2
        instruct = instruct + ' ' + str(random.randint(1, pid + 1)) + ' ' + str(random.randint(1, gid + 1))
    else:   # qps / qbs
        pass
    return instruct


def generateInstSet(num):
    inst_set = ''
    for i in range(7):
        ap_inst = 'ap' + ' ' + str(i + 1) + ' ' + str(i + 1000) + ' ' + str(random.randint(1, 100)) + '\n'
        inst_set += ap_inst
    for i in range(num):
        inst_set += generateOneInst() + '\n'
    return inst_set


def executeJava(stdin, jar_name):
    cmd = ['java', '-jar', jar_name]
    proc = subprocess.Popen(cmd, stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    stdout, stderr = proc.communicate(stdin.encode())
    return stdout.decode()


inst_size = 1000    # 设置指令条数
test_size = 200     # 设置测试次数
for test in range(test_size):
    data = generateInstSet(inst_size)
    out1 = executeJava(data, 'hw9.jar').replace("\r", "")   # some package may end with \r\n
    out2 = executeJava(data, 'Alterego.jar').replace("\r", "")
    out1List = out1.split('\n')
    out2List = out2.split('\n')

    print("[Test", test + 1, "begin]")  # emphasize the beginning of a test
    flag = 0
    if len(out1List) != len(out2List):
        print("line num different")
        flag = 1
    else:
        for i in range(len(out1List)):
            if out1List[i] != out2List[i]:
                print("wrong in line {0}".format(str(i + 1)))
                print("instr: " + data.split('\n')[i])
                print('std: ' + out1List[i])
                print('sel: ' + out2List[i])
                print(out1List[i].strip() == out2List[i].strip())
                flag = 1
    if flag == 0:
        print(str(test + 1) + " accept!")
    else:                   # stop and print data while catching error
        print(data)
        break
