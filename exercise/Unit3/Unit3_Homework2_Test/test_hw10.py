import random
import subprocess

pid = 8
gid = 1
mid = 1


def generateOneInst():
    # rate_set = [15, 15, 5, 4, 4, 6, 7, 4, 4, 4, 4, 4, 5, 5, 4, 5, 5]       # normal
    # rate_set = [25, 20, 0, 3, 3, 1, 2, 3, 3, 30, 3, 3, 0, 0, 1, 0, 3]     # qlc
    # rate_set = [15, 15, 0, 2, 1, 1, 15, 15, 1, 10, 3, 3, 0, 0, 1, 15, 3]
    rate_set = [15, 15, 0, 0, 0, 0, 15, 15, 15, 0, 0, 0, 0, 0, 0, 25, 0]  # qgvs
    global pid
    global gid
    global mid
    instruct_set = ['ap', 'ar', 'qv', 'qps', 'qci', 'qbs', 'ag', 'atg', 'dfg', 'qlc', 'qrm', 'qsv', 'sm', 'am', 'qgav',
                    'qgvs', 'qgps']
    choose = random.randint(1, 100)
    index = 0
    sum_index = 0
    while sum_index < choose:
        sum_index += rate_set[index]
        index = index + 1
    instruct = instruct_set[index - 1]

    if index == 1:      # ap id name age
        instruct = instruct + ' ' + str(pid) + ' ' + str(random.randint(1, 9999999999)) + ' ' + str(
            random.randint(0, 200))
        pid += 1
    elif index == 2:    # ar id1 id2 value
        instruct = instruct + ' ' + str(random.randint(1, pid + 1)) + ' ' + str(
            random.randint(1, pid + 1)) + ' ' + str(
            random.randint(0, 1000))
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
    elif index == 10:   # qlc id
        instruct = instruct + ' ' + str(random.randint(1, pid))
    elif index == 11:   # qrm id
        instruct = instruct + ' ' + str(random.randint(1, pid))
    elif index == 12:   # qsv id
        instruct = instruct + ' ' + str(random.randint(1, pid))
    elif index == 13:   # sm mid
        instruct = instruct + ' ' + str(random.randint(1, mid + 1))
    elif index == 14:   # am mid
        instruct = instruct + ' ' + str(mid)
        typeInt = random.randint(0, 1)
        if typeInt != 1:    # am mid socialValue type(0) pid pid
            instruct = instruct + ' ' + str(random.randint(-1000, 1000)) + ' ' + str(typeInt) + ' ' + \
                       str(random.randint(1, pid + 1)) + ' ' + str(random.randint(1, pid + 1))
        else:   # am mid socialValue type(1) pid gid
            instruct = instruct + ' ' + str(random.randint(-1000, 1000)) + ' ' + str(typeInt) + ' ' + \
                       str(random.randint(1, pid + 1)) + ' ' + str(random.randint(1, gid + 1))
        mid += 1
    elif index == 15:  # qgav gid
        instruct = instruct + ' ' + str(random.randint(1, gid + 1))
    elif index == 16:  # qgvs gid
        instruct = instruct + ' ' + str(random.randint(1, gid + 1))
    elif index == 17:  # qgps gid
        instruct = instruct + ' ' + str(random.randint(1, gid + 1))
    else:   # qps / qbs
        pass
    return instruct


def generateInstSet(num):
    inst_set = ''
    for i in range(7):
        ap_inst = 'ap' + ' ' + str(i + 1) + ' ' + str(i + 1000) + ' ' + str(random.randint(1, 100)) + '\n'
        inst_set += ap_inst
    global pid
    global gid
    global mid
    pid, gid, mid = 8, 1, 1     # init
    for i in range(num):
        inst_set += generateOneInst() + '\n'
    return inst_set


def executeJava(stdin, jar_name):
    cmd = ['java', '-jar', jar_name]
    proc = subprocess.Popen(cmd, stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    stdout, stderr = proc.communicate(stdin.encode())
    return stdout.decode()


inst_size = 10000    # 设置指令条数
test_size = 1000     # 设置测试次数
for test in range(test_size):
    data = generateInstSet(inst_size)
    # print(data)
    out1 = executeJava(data, 'hw10_zwh.jar').replace("\r", "").strip()   # some package may end with \r\n
    out2 = executeJava(data, 'Rider.jar').replace("\r", "").strip()   # 你的jar包
    out1List = out1.split('\n')
    out2List = out2.split('\n')

    print("[Test", test + 1, "begin]")  # emphasize the beginning of a test
    flag = 0
    if len(out1List) != len(out2List):
        print("line num different")
        flag = 1
    else:
        for i in range(len(out1List) - 1):
            if out1List[i] != out2List[i]:
                print("wrong in line {0}".format(str(i + 1)))
                print("instr: " + data.split('\n')[i])
                print('std: ' + out1List[i])
                print('sel: ' + out2List[i])
                print(out1List[i].strip() == out2List[i].strip())
                flag = 1
        print("time: std-" + out1List[-1] + " sel-" + out2List[-1])
    if flag == 0:
        print(str(test + 1) + " accept!")
    else:                   # stop and print data while catching error
        print(data)
        break
