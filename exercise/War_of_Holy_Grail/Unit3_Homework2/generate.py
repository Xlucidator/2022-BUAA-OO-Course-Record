import imp


import random

# data = ""
# for i in range(1111):
#     data = data + "ap " + str(i) + " " + str(i) + " 1\n"
# data = data + "ag 1\n"
# for i in range(1111):
#     data = data + "atg " + str(i) + " 1\n"
# for i in range(2777):
#     data = data + "qgvs 1\n"

# f = open("hack1_1111ap+1111atg+2777qgvs.txt", "w")
# f.write(data)
# f.close()

data = ""
for i in range(1112):
    data = data + "ap " + str(i) + " " + str(i) + " " + str(random.randint(1, 200)) + "\n"
data = data + "ag 1\n"
for i in range(1112):
    data = data + "atg " + str(i) + " 1\n"
data = data + "qgps 1\n"
for i in range(2774):
    data = data + "qgav 1\n"

f = open("hack2_1112ap+1112atg+2774qgav.txt", "w")
f.write(data)
f.close()
