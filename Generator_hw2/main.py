import os

os.system('del input.txt')
os.system('del output1.txt')
os.system('del output2.txt')

for k in range(100):
    n = 1
    os.system('java -jar DataGenerator.jar > 0.txt')
    os.system('java -jar lzq.jar < 0.txt > 1.txt')
    os.system('java -jar src6.jar < 0.txt > 2.txt')
    os.system('type 0.txt >> input.txt')
    os.system('echo. >> input.txt')
    os.system('type 1.txt >> output1.txt')
    os.system('type 2.txt >> output2.txt')
    os.system('python Judge.py')
    print(f'test{k} finish')
