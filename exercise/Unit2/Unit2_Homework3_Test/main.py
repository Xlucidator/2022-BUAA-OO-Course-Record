import random


def judgeMask(mask):
    return (mask & 1) + ((mask >> 1) & 1) + ((mask >> 2) & 1) + ((mask >> 3) & 1) + ((mask >> 4) & 1) >= 2


person_id = 0
elevator_id = 6
types = ['building', 'floor']
buildings = ['A', 'B', 'C', 'D', 'E']
floors = list(range(1, 11))
capacity = [4, 6, 8]
speed = [0.4, 0.6]
access = [m for m in range(1, 32) if judgeMask(m)]


def generatePersonRequest():
    global person_id

    person_id += 1
    from_building, to_building = random.choice(buildings), random.choice(buildings)
    from_floor, to_floor = random.choice(floors), random.choice(floors)
    while from_building == to_building and from_floor == to_floor:
        to_floor = random.choice(floors)

    return str(person_id) + '-FROM-' + from_building + '-' + str(from_floor) \
                          + '-TO-' + to_building + '-' + str(to_floor)


def generateElevatorRequest():
    global elevator_id

    elevator_id += 1
    e_type = random.choice(types)
    e_capacity = random.choice(capacity)
    e_speed = random.choice(speed)

    part_inst = '-' + str(e_capacity) + '-' + str(e_speed)
    if e_type == 'building':
        e_build_no = random.choice(buildings)
        return 'ADD-building-' + str(elevator_id) + '-' + e_build_no + part_inst
    else:
        e_floor_no = random.choice(floors)
        e_access = random.choice(access)
        return 'ADD-floor-' + str(elevator_id) + '-' + str(e_floor_no) + part_inst + '-' + str(e_access)


def generateInst(num):
    time_seq = []
    for t in range(num):
        time_seq.append(random.randint(10, 400) / 10)
    time_seq.sort()

    for i in range(num):
        r = random.random()
        inst = ''
        if r < 0.6:
            inst += '[' + str(time_seq[i]) + ']' + generatePersonRequest()
        else:
            inst += '[' + str(time_seq[i]) + ']' + generateElevatorRequest()
        print(inst)


generateInst(30)
