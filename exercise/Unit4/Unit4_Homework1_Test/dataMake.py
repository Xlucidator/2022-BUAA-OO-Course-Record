from randomUmlMake import RandomUmlMake
from randomUmlMake import methodsName
from randomUmlMake import makeClassName
from setting import *
import sys
import random


def createClassCount():
    return 'CLASS_COUNT'


def createClassSubCount(className):
    return 'CLASS_SUBCLASS_COUNT ' + className


def createClassOpCount(className):
    return 'CLASS_OPERATION_COUNT ' + className


def createClassOpVis(className, methodName):
    return 'CLASS_OPERATION_VISIBILITY ' + className + ' ' + methodName


def createClassOpCoupling(className, methodName):
    return 'CLASS_OPERATION_COUPLING_DEGREE ' + className + ' ' + methodName


def createClassCoupling(className):
    return 'CLASS_ATTR_COUPLING_DEGREE ' + className


def createClassImplementList(className):
    return 'CLASS_IMPLEMENT_INTERFACE_LIST ' + className


def createClassInheritDepth(className):
    return 'CLASS_DEPTH_OF_INHERITANCE ' + className

# def StrongData(count='1', mode='normal'):
#     with randomUmlMake(count) as m:
#         instrs = []
#         if (mode == 'normal'):
#             model = m[0]
#             datafile = m[1]
#             classidMap = model.getClass()
#             interfaceidMap = model.getInterface()
#             for classid in classidMap:
#                 className = classidMap[classid]
#                 attrnames = []
#                 id = classid
#                 while (id != None):
#                     attrnames.extend(model.getClassAttributes(id))
#                     id = model.getClassParentId(id)
#                 attrnames = set(attrnames)
#                 for i in range(normalCount):
#                     instrs.extend(createClassAttrCount(className))
#                     instrs.append(createClassAssoCount(className))
#                     instrs.append(createClassAssoClassList(className))
#                     for s in attrnames:
#                         instrs.append(createAttrVisibility(className, s))
#                     instrs.append(createClassTop(className))
#         elif (mode == 'strong'):
#             model = m[0]
#             datafile = m[1]
#             classidMap = model.getClass()
#             interfaceidMap = model.getInterface()
#             for classid in classidMap:
#                 className = classidMap[classid]
#                 for i in range(strongCount):
#                     instrs.append(createImpleInterList(className))
#                     # instrs.append(createInfoHidden(className))
#         for instr in range(len(instrs) - 1):
#             datafile.write(instrs[instr] + '\n')
#         datafile.write(instrs[-1])


def randomMake(count='1'):
    instrs = []
    with RandomUmlMake(count) as m:
        model = m[0]
        datafile = m[1]
        classidMap = model.getClass()
        interfaceidMap = model.getInterface()
        instrs.append(createClassCount())
        if (notFoundClass):
            notFoundClassName = random.randint(len(classidMap), 99999)
            notFoundClassName = makeClassName(notFoundClassName)
            instrs.append(createClassCount())
            instrs.append(createClassImplementList(notFoundClassName))
            instrs.append(createClassInheritDepth(notFoundClassName))
            instrs.append(createClassOpCount(notFoundClassName))
            instrs.append(createClassCoupling(notFoundClassName))
            instrs.append(createClassSubCount(notFoundClassName))
            instrs.append(createClassOpCoupling(notFoundClassName, notFoundClassName))
            instrs.append(createClassOpVis(notFoundClassName, notFoundClassName))
        for i in classidMap:
            className = classidMap[i]
            instrs.append(createClassOpCount(className))
            instrs.append(createClassInheritDepth(className))
            instrs.append(createClassImplementList(className))
            instrs.append(createClassCoupling(className))
            instrs.append(createClassSubCount(className))
            methods = list(methodsName)
            for method in methods:
                instrs.append(createClassOpCoupling(className, method))
                instrs.append(createClassOpVis(className, method))
        strs = ""
        for i in range(len(instrs)):
            # if (i != len(instrs) - 1):
            strs += instrs[i] + '\n'
            datafile.write(instrs[i] + '\n')
            # else:
            #     datafile.write(instrs[i])
    return strs


def dataMake(count='1', mode='normal', isStrong=False):
    # if (isStrong):
    #     StrongData(count, mode)
    # else:
    randomMake(count)
    f = open("data.json", "r")
    stdin = f.read().replace('\r', '')
    f.close()
    print('Data gen finish!')
    return stdin


if __name__ == "__main__":
    if (len(sys.argv) < 3):
        dataMake()
    else:
        dataMake(sys.argv[1], sys.argv[2], isStrong=True)
