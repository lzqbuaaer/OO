from sympy import symbols, exp, Eq, simplify
from sympy.parsing.sympy_parser import parse_expr
def evaluate_expression(expression, x):
    # 替换表达式中的乘方符号
    expression = expression.replace('^', '**')
    # 将表达式中的x替换为给定的x值
    expression = expression.replace('x', str(x))
    # 使用eval函数计算表达式的值
    return eval(expression)

def compare_expressions(expression1, expression2):
    x, y, z = symbols('x y z')
    list = {-237, 0, 131}
    for i in list:
        for j in list:
            for k in list:
                expr1 = parse_expr(expression1.replace('^', '**'))
                expr2 = parse_expr(expression2.replace('^', '**'))
                expr1 = expr1.subs([(x, i), (y, j), (z, k)])
                expr2 = expr2.subs([(x, i), (y, j), (z, k)])
                if not Eq(simplify(expr1), simplify(expr2)):
                    return False
    return True

def check_equivalence(file1, file2):
    # 打开文件1和文件2
    with open(file1, 'r') as f1, open(file2, 'r') as f2:
        # 逐行读取两个文件的内容
        lines1 = f1.readlines()
        lines2 = f2.readlines()
    # 判断两个文件的行数是否相同
    if len(lines1) != len(lines2):
        return False

    # 遍历每一行表达式进行等价性判断
    for i in range(len(lines1)):
        # 去除行末的换行符
        expression1 = lines1[i].strip()
        expression2 = lines2[i].strip()

        # 遍历给定的x值进行等价性判断
        for x in range(-5, 6):
            if compare_expressions(expression1, expression2):
                return True
    return False

def check(file1, file2):
    # 调用函数检查等价性
    result = check_equivalence(file1, file2)
    if result:
        print("out1.txt的表达式与out2.txt相同行的表达式等价")
    else:
        raise Exception("out1.txt的表达式与out2.txt相同行的表达式不等价")
try:
    check("output1.txt", "output2.txt")
except Exception as e:
    print('\033[31m')
    print(e)
    exit()