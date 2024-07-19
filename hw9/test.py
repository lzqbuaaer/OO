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
        print(50,end='')
        print(' ',end='')
    print('')

for i in range(2800):
    print('mr ' + str(random.randint(1,100)) + ' ' + str(random.randint(1,100)) + ' -30')