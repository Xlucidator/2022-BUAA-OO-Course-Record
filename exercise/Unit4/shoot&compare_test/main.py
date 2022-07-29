import subprocess
import dataMake


def execJava(stdin, jarName):
    cmd = 'java -jar ' + jarName
    proc = subprocess.Popen(cmd, stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    out, err = proc.communicate(stdin.encode())
    return out.decode()


test_size = 200

for test in range(1, test_size + 1):
    flag = 0
    print("[Test " + str(test) + " Begin]")
    stdin = dataMake.dataMake('1', 'normal', False)
    out1List = execJava(stdin, 'hw13_zwh.jar').replace('\r', '').split('\n')
    out2List = execJava(stdin, 'hw13_xrb.jar').replace('\r', '').split('\n')
    # print(out1List)
    if len(out1List) != len(out2List):
        print("line num different! \033[31mSee jar*_output.txt for details\033[0m")
        print("out1LineNum:" + str(len(out1List)))
        print("out2LineNum:" + str(len(out2List)))
        f1 = open('jar1_output.txt', 'w')
        f2 = open('jar2_output.txt', 'w')
        for s in out1List:
            f1.write(s)
            f1.write('\n')
        for s in out2List:
            f2.write(s)
            f2.write('\n')
        f1.close()
        f2.close()
        flag = 1
    else:
        for i in range(len(out1List)):
            if out1List[i] != out2List[i]:
                print('different in line ' + str(i + 1))
                print('out1:' + out1List[i])
                print('out2: ' + out2List[i])
                flag = 1

    if flag == 0:
        print("\033[32mTest Accepted!\033[0m")
    else:
        print("\033[31mTest Failed! See data.json for stdin.\033[0m")
        break

# dataMake.dataMake('1', 'normal', False)
