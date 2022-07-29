import random
import subprocess
import os

pid = 8
gid = 1
mid = 1
eid = 1

weight_set = {
    'ap': 20, 'ar': 20, 'qv': 5, 'qps': 5, 'qci': 10, 'qbs': 7, 'ag': 15,
    'atg': 20, 'dfg': 2, 'qlc': 20, 'qrm': 5, 'qsv': 5, 'sm': 15, 'am': 15,
    'qgav': 2, 'qgvs': 20, 'qgps': 20, 'arem': 6, 'anm': 6, 'cn': 4, 'aem': 6,
    'sei': 5, 'qp': 5, 'dce': 5, 'qm': 5, 'sim': 50
}


def generateOneInst():
    global pid, gid, mid, eid
    inst = random.choices(list(weight_set.keys()), weights=list(weight_set.values()), k=1)[0]

    if inst == "ap":  # ap id name age
        inst += ' ' + str(pid) + ' ' + str(random.randint(1, 9999999999)) + ' ' + str(
            random.randint(0, 200))
        pid += 1
    elif inst == "ar":  # ar id1 id2 value
        inst += ' ' + str(random.randint(1, pid)) + ' ' + str(random.randint(1, pid)) \
                   + ' ' + str(random.randint(0, 1000))
    elif inst == "qv":  # qv id1 id2
        inst += ' ' + str(random.randint(1, pid + 1)) + ' ' + str(random.randint(1, pid + 1))
    elif inst == "qci":  # qci id1 id2
        inst += ' ' + str(random.randint(1, pid + 1)) + ' ' + str(random.randint(1, pid + 1))
    elif inst == "ag":  # ag id
        inst += ' ' + str(random.randint(1, gid))
        gid += 1
    elif inst == "atg":  # atg id1 id2
        inst += ' ' + str(random.randint(1, pid)) + ' ' + str(random.randint(1, gid))
    elif inst == "dfg":  # dfg id1 id2
        inst += ' ' + str(random.randint(1, pid)) + ' ' + str(random.randint(1, gid))
    elif inst == "qlc":  # qlc id
        inst += ' ' + str(random.randint(1, pid))
    elif inst == "qrm":  # qrm id
        inst += ' ' + str(random.randint(1, pid))
    elif inst == "qsv":  # qsv id
        inst += ' ' + str(random.randint(1, pid))
    elif inst == "sm":  # sm mid
        inst += ' ' + str(random.randint(1, mid + 1))
    elif inst == "am":  # am mid
        inst += ' ' + str(mid)
        type_int = random.randint(0, 1)
        if type_int != 1:  # am mid socialValue type(0) pid pid
            inst += ' ' + str(random.randint(-1000, 1000)) + ' ' + str(type_int) + ' ' + \
                       str(random.randint(1, pid + 1)) + ' ' + str(random.randint(1, pid + 1))
        else:  # am mid socialValue type(1) pid gid
            inst += ' ' + str(random.randint(-1000, 1000)) + ' ' + str(type_int) + ' ' + \
                       str(random.randint(1, pid + 1)) + ' ' + str(random.randint(1, gid + 1))
        mid += 1
    elif inst == "qgav":  # qgav gid
        inst += ' ' + str(random.randint(1, gid + 1))
    elif inst == "qgvs":  # qgvs gid
        inst += ' ' + str(random.randint(1, gid))
    elif inst == "qgps":  # qgps gid
        inst += ' ' + str(random.randint(1, gid + 1))
    elif inst == "arem":  # arem mid
        inst += ' ' + str(mid)
        type_int = random.randint(0, 1)
        if type_int != 1:  # arem mid money type(0) pid pid
            inst += ' ' + str(random.randint(-1000, 1000)) + ' ' + str(type_int) + ' ' + \
                       str(random.randint(1, pid + 1)) + ' ' + str(random.randint(1, pid + 1))
        else:  # arem mid money type(1) pid gid
            inst += ' ' + str(random.randint(-1000, 1000)) + ' ' + str(type_int) + ' ' + \
                       str(random.randint(1, pid + 1)) + ' ' + str(random.randint(1, gid + 1))
        mid += 1
    elif inst == "anm":  # anm mid
        inst += ' ' + str(mid)
        type_int = random.randint(0, 1)
        if type_int != 1:  # anm mid str type(0) pid pid
            inst += ' ' + str(random.randint(-1000, 1000)) + ' ' + str(type_int) + ' ' + \
                       str(random.randint(1, pid + 1)) + ' ' + str(random.randint(1, pid + 1))
        else:  # anm mid str type(1) pid gid
            inst += ' ' + str(random.randint(-1000, 1000)) + ' ' + str(type_int) + ' ' + \
                       str(random.randint(1, pid + 1)) + ' ' + str(random.randint(1, gid + 1))
        mid += 1
    elif inst == "cn":  # cn pid
        inst += ' ' + str(random.randint(1, pid + 1))
    elif inst == "aem":  # aem mid
        inst += ' ' + str(mid)
        type_int = random.randint(0, 1)
        if type_int != 1:  # aem mid emojiId type(0) pid pid
            inst += ' ' + str(random.randint(1, eid)) + ' ' + str(type_int) + ' ' + \
                       str(random.randint(1, pid + 1)) + ' ' + str(random.randint(1, pid + 1))
        else:  # aem mid emojiId type(1) pid gid
            inst += ' ' + str(random.randint(1, eid)) + ' ' + str(type_int) + ' ' + \
                       str(random.randint(1, pid + 1)) + ' ' + str(random.randint(1, gid + 1))
    elif inst == "sei":  # sei eid
        inst += ' ' + str(eid)
        eid += 1
    elif inst == "qp":  # qp eid
        inst += ' ' + str(random.randint(1, eid))
    elif inst == "dce":  # dce limit
        inst += ' ' + str(random.randint(1, 3))
    elif inst == "qm":  # qm pid
        inst += ' ' + str(random.randint(1, pid + 1))
    elif inst == "sim":  # sim mid
        inst += ' ' + str(random.randint(1, mid + 1))
    else:  # qps / qbs
        pass
    return inst


def generateInstSet(num):
    inst_set = ''
    for j in range(7):
        ap_inst = 'ap' + ' ' + str(j + 1) + ' ' + str(j + 1000) + ' ' + str(random.randint(1, 100)) + '\n'
        inst_set += ap_inst
    for j in range(2):
        ap_inst = 'ag' + ' ' + str(j + 1) + '\n'
        inst_set += ap_inst
    global pid, gid, mid
    pid, gid, mid = 8, 1, 1  # init
    for j in range(num):
        inst_set += generateOneInst() + '\n'
    return inst_set


def getFileText(filename):
    if not os.path.exists(filename):
        print("ERROR: file not exit: %s" % filename)
        return None
    f = open(filename, "r")
    content = f.read().replace("\r", "")
    f.close()
    return content


def executeJava(stdin, jar_name):
    cmd = ['java', '-jar', jar_name]
    proc = subprocess.Popen(cmd, stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    stdout, stderr = proc.communicate(stdin.encode())
    return stdout.decode()


def compareOutput(out_list1, out_list2):
    if len(out_list1) != len(out_list2):
        print("line num different")
        return False
    else:
        res = True
        for j in range(len(out_list1) - 1):
            if out_list1[j] != out_list2[j]:
                print("wrong in line {0}".format(str(j + 1)))
                print("instr: " + data.split('\n')[j])
                print('std: ' + out_list1[j])
                print('sel: ' + out_list2[j])
                print(out_list1[j].strip() == out_list2[j].strip())
                res = False
        print("time: std-" + out_list1[-1] + " sel-" + out_list2[-1])
        return res


inst_size = 10000  # 设置指令条数
test_size = 1000  # 设置测试次数
test_mode = "auto"  # 设置测试模式 ["auto", "manual"]
test_file = "stdin.txt"  # 自备测试stdin文件

cmp_case1 = "hw11_zwh_new.jar"  # 对拍文件
cmp_case2 = "hw11_xrb_new.jar"

if test_mode == "manual":
    print("[Manual Test Begin]\ninput_file: " + test_file)
    data = getFileText(test_file)
    # print(data)
    out1List = executeJava(data, cmp_case1).replace("\r", "").strip().split("\n")  # some package may end with \r\n
    out2List = executeJava(data, cmp_case2).replace("\r", "").strip().split("\n")  # 你的jar包
    flag = compareOutput(out1List, out2List)
    print("test accept!" if flag else "test fail")
else:
    print("[Auto Test Begin]")
    for test in range(test_size):
        data = generateInstSet(inst_size)
        # print(data)
        # print('gid:' + str(gid))
        out1List = executeJava(data, cmp_case1).replace("\r", "").strip().split("\n")
        out2List = executeJava(data, cmp_case2).replace("\r", "").strip().split("\n")
        print("[Test", test + 1, "begin]")  # emphasize the beginning of a test
        flag = compareOutput(out1List, out2List)
        if flag:
            print(str(test + 1) + " accept!")
        else:  # stop and print data while catching error
            print(data)
            break
