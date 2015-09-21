class GherkinTable(object):

    def __init__(self, gherkin_text: str):
        self._table = self._parse(gherkin_text)

    def transpose(self):
        self._table = list(zip(*self._table))

    def toString(self):
        column_sizes = [max([len(item) for item in row])
                        for row in zip(*self._table)]

        result = ''
        for row in self._table:
            result += '|'
            for i, e in enumerate(row):
                result += ' %s |' % e.ljust(column_sizes[i])
            result += '\n'

        return str(result)

    def __str__(self):
        return self.toString()

    def __repr__(self):
        return self.toString()

    def _parse(self, gherkin_text: str):
        lines = gherkin_text.splitlines()
        return [[item.strip() for item in line.strip("| ").split("|")]
                for line in lines]
