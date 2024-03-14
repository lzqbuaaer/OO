import random


class Generator:
    def __init__(self, mark=0):
        self.isFuncExpr = mark

    def exprGenerator(self):
        expr = self.spaceGenerator()
        expr += self.plusMinusGenerator()
        expr += self.spaceGenerator()
        expr += self.termGenerator()
        expr += self.spaceGenerator()

        n = random.randint(0, 5)
        for i in range(n):
            expr += self.plusMinusGenerator(0)
            expr += self.spaceGenerator()
            expr += self.termGenerator()
            expr += self.spaceGenerator()
        return expr

    def termGenerator(self):
        term = self.plusMinusGenerator()
        term += self.spaceGenerator()
        term += self.factorGenerator()

        n = random.randint(0, 5)
        for i in range(n):
            term += self.spaceGenerator()
            term += '*'
            term += self.spaceGenerator()
            term += self.factorGenerator()
        return term

    def factorGenerator(self):
        factor = ''
        n = random.random()
        if n < 0.33:
            factor += self.numGenerator()
        elif n < 0.67:
            factor += self.powGenerator()
        else:
            factor += self.expfacGenerator()
        return factor

    def expGenerator(self):
        ans = 'exp'
        ans += self.spaceGenerator()
        ans += '('
        ans += self.factorGenerator()
        ans += ')'
        n = random.randint(0, 6)
        r = random.random()
        if r < 0.2:
            ans += '^ 0'
        elif r < 0.8:
            ans += '^'
            if r < 0.5:
                ans += '+'
            ans += str(n)
        return ans

    def numGenerator(self):
        ans = self.plusMinusGenerator()
        n = random.random()
        if n < 0.5:
            ans += '0'
        n = random.random()
        if n > 0.2:
            num = random.randint(0, 10000)
            ans += str(num)
        else:
            ans += '0'
        return ans

    def powGenerator(self):
        ans = 'x'
        r = random.random()
        n = random.randint(0, 6)
        if r < 0.2:
            ans += '^ 0'
        elif r < 0.8:
            ans += '^'
            if r < 0.5:
                ans += '+'
            ans += str(n)
        return ans

    def expfacGenerator(self):
        ans = '('
        gen = Generator(1)
        ans += gen.exprGenerator()
        ans += ')'
        n = random.randint(0, 4)
        r = random.random()
        if r < 0.2:
            ans += '^ 0'
        elif r < 0.8:
            ans += '^'
            if r < 0.5:
                ans += '+'
            ans += str(n)
        return ans

    def spaceGenerator(self):
        n = random.randint(0, 1)
        space = n * ' '
        return space

    def plusMinusGenerator(self, canSpace=1):
        n = random.random()
        ans = ''
        if canSpace == 1:
            if n < 0.25:
                ans += '+'
            elif n < 0.75:
                ans += '-'
        else:
            if n < 0.5:
                ans += '+'
            else:
                ans += '-'
        return ans


if __name__ == '__main__':
    gen = Generator(0)
    expr = gen.exprGenerator()
    print(expr)
