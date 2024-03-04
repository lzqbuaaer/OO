import os
for k in range(1):
    os.system('del input.txt')
    os.system('del output1.txt')
    os.system('del output2.txt')
    n = 10
    for i in range(n):
        os.system('python Generator.py > 0.txt')  #os.system('java -jar DataGenerator.jar > 0.txt')  # 两个不同的评测姬，强度差不多
        os.system('java -jar lzq.jar < 0.txt > 1.txt')
        os.system('java -jar src5.jar < 0.txt > 2.txt')
        os.system('type 0.txt >> input.txt')
        os.system('echo. >> input.txt')
        os.system('type 1.txt >> output1.txt')
        os.system('type 2.txt >> output2.txt')
        print(f'test{i} finish')
    os.system('python Judge.py')



