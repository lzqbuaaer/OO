import random
print('ln 100')
for i in range(100):
    print(i + 1,end='')
    print(' ',end='')
print('')
for i in range(100):
    print(i + 1,end='')
    print(' ',end='')
print('')
for i in range(100):
    print(50,end='')
    print(' ',end='')
print('')
for i in range(99):
    for j in range(i+1):
        print(random.randint(1,100),end='')
        print(' ',end='')
    print('')
print('at 1 1')
for i in range(99):
    print('att ' + str(i+2) + ' 1 1')

for i in range(1400):
    id1 = random.randint(2,100)
    id2 = random.randint(2,100)
    print('mr ' + str(id1) + ' ' + str(id2) + ' -60')
    print('qtvs 1 1')
  

# for i in range(8000):
#     print('mr ' + str(random.randint(1,100)) + ' ' + str(random.randint(1,100)) + ' -100')

# for i in range(5000):
#     print('qsp ' + str(random.randint(1,100)) + ' ' + str(random.randint(1,100)))