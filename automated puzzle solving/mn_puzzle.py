from puzzle import Puzzle


class MNPuzzle(Puzzle):
    """
    An nxm puzzle, like the 15-puzzle, which may be solved, unsolved,
    or even unsolvable.
    """

    def __init__(self, from_grid, to_grid):
        """
        MNPuzzle in state from_grid, working towards
        state to_grid

        @param MNPuzzle self: this MNPuzzle
        @param tuple[tuple[str]] from_grid: current configuration
        @param tuple[tuple[str]] to_grid: solution configuration
        @rtype: None
        """
        # represent grid symbols with letters or numerals
        # represent the empty space with a "*"
        assert len(from_grid) > 0
        assert all([len(r) == len(from_grid[0]) for r in from_grid])
        assert all([len(r) == len(to_grid[0]) for r in to_grid])
        self.n, self.m = len(from_grid), len(from_grid[0])
        self.from_grid, self.to_grid = from_grid, to_grid

    # TODO
    # implement __eq__ and __str__
    # __repr__ is up to you
    
    def __eq__(self, other):
        """
        Return whether MNPuzzle self is equivalent to other.

        @type self: MNPuzzle
        @type other: MNPuzzle | Any
        @rtype: bool

        >>> target_grid1 = (("1", "2", "3"), ("4", "5", "*"))
        >>> start_grid1 = (("*", "2", "3"), ("1", "4", "5"))
        >>> M1 = MNPuzzle(start_grid1, target_grid1)
        >>> target_grid2 = (("1", "2", "3"), ("4", "5", "*"))
        >>> start_grid2 = (("*", "2", "3"), ("1", "4", "5"))
        >>> M2 = MNPuzzle(start_grid2, target_grid2)
        >>> M1 == M2
        True
        >>> target_grid3 = (("1", "2", "*"), ("4", "5", "*"))
        >>> start_grid3 = (("*", "2", "3"), ("1", "4", "5"))
        >>> M3 = MNPuzzle(start_grid3, target_grid3)
        >>> M1 == M3
        False
        """
        return (type(self) == type(other) and
                self.from_grid == other.from_grid and
                self.to_grid == other.to_grid)
    
    def __str__(self):
        """
        Return a human-readable string representation of MNPuzzle
        self.

        >>> target_grid = (("1", "2", "3"), ("4", "5", "*"))
        >>> start_grid = (("*", "2", "3"), ("1", "4", "5"))
        >>> M = MNPuzzle(start_grid, target_grid)
        >>> print(M)
        * 2 3 
        1 4 5 
        """
        output = ""

        for row in self.from_grid:
            for item in row:
                output += item + " "
            output += "\n"
        return output.strip("\n")

    # TODO
    # override extensions
    # legal extensions are configurations that can be reached by swapping one
    # symbol to the left, right, above, or below "*" with "*"
    def extensions(self):
        """
        Return a list of extensions of MNPuzzle self.

        @type self: MNPuzzle 
        @rtype: list[MNPuzzle]
        """
        extensions = []
        #convenient name
        current = self.from_grid
        height, width = len(current), len(current[0])
        index = self.find_index()
        
        #Up and Down
        if (index[0] > 0 and index[0] < height-1):
            extensions += [self.swap_up(index)] + [self.swap_down(index)]
        #Down
        elif(index[0] == 0 and index[0] < height):
            extensions += [self.swap_down(index)]
        #Up
        elif(index[0] > 0 and index[0] == height):
            extensions += [self.swap_up(index)] 

        #Left and Right
        if (index[1] > 0 and index[1] < width-1):
            extensions += [self.swap_right(index)] + [self.swap_left(index)]
        #Right
        elif(index[1] == 0 and index[1] != width):
            extensions += [self.swap_right(index)]
        #Left
        elif(index[1] != 0 and index[1] == width):
            extensions += [self.swap_left(index)]
        return extensions
    
    def swap_up(self, index):
        """
        Swap "*" at position index with the item symbol above it

        @type self: MNPuzzle
        @type index: tuple[int]
        @rtype: MNPuzzle

        >>> target_grid = (("1", "2", "3"), ("4", "5", "*"))
        >>> start_grid = (('2', '4', '3'), ('1', '*', '5'))
        >>> M = MNPuzzle(start_grid, target_grid)
        >>> new_M = M.swap_up((0, 1))
        >>> print(new_M.from_grid)
        (('2', '*', '3'), ('1', '4', '5'))
        """
        grid = self.from_grid
        new_grid = []
        for row in grid:
            new_grid.append(list(row))
        
        new_grid[index[0] - 1][index[1]], new_grid[index[0]][index[1]] = \
                           new_grid[index[0]][index[1]], new_grid[index[0] - 1][
                               index[1]]
        for i in range(len(grid)):
            new_grid[i] = tuple(new_grid[i])

        return MNPuzzle(tuple(new_grid), self.to_grid)
        
        
    def swap_down(self, index):
        """
        Swap "*" at position index with the item symbol below it

        @type self: MNPuzzle
        @type index: tuple[int]
        @rtype: MNPuzzle

        >>> target_grid = (("1", "2", "3"), ("4", "5", "*"))
        >>> start_grid = (('2', '*', '3'), ('1', '4', '5'))
        >>> M = MNPuzzle(start_grid, target_grid)
        >>> new_M = M.swap_down((0, 1))
        >>> print(new_M.from_grid)
        (('2', '4', '3'), ('1', '*', '5'))
        """
        grid = self.from_grid
        new_grid = []
        for row in grid:
            new_grid.append(list(row))
        
        new_grid[index[0] + 1][index[1]], new_grid[index[0]][index[1]] = \
                new_grid[index[0]][index[1]], new_grid[index[0] + 1][index[1]]
        for i in range(len(grid)):
            new_grid[i] = tuple(new_grid[i])

        return MNPuzzle(tuple(new_grid), self.to_grid)
        
    def swap_left(self, index):
        """
        Swap "*" at position index with the item symbol to the left of it

        @type self: MNPuzzle
        @type index: tuple[int]
        @rtype: MNPuzzle

        >>> target_grid = (("1", "2", "3"), ("4", "5", "*"))
        >>> start_grid = (('2', '*', '3'), ('1', '4', '5'))
        >>> M = MNPuzzle(start_grid, target_grid)
        >>> new_M = M.swap_left((0, 1))
        >>> print(new_M.from_grid)
        (('*', '2', '3'), ('1', '4', '5'))
        """
        grid = self.from_grid
        new_grid = []
        for row in grid:
            new_grid.append(list(row))
        
        new_grid[index[0]][index[1]], new_grid[index[0]][index[1] -1] = \
                           new_grid[index[0]][index[1] -1], new_grid[index[0]][
                               index[1]]
        for i in range(len(grid)):
            new_grid[i] = tuple(new_grid[i])
        
        return MNPuzzle(tuple(new_grid), self.to_grid)
        
    def swap_right(self, index):
        """
        Swap "*" at position index with the item symbol to the right of it

        @type self: MNPuzzle
        @type index: tuple[int]
        @rtype: MNPuzzle
        
        >>> target_grid = (("1", "2", "3"), ("4", "5", "*"))
        >>> start_grid = (("*", "2", "3"), ("1", "4", "5"))
        >>> M = MNPuzzle(start_grid, target_grid)
        >>> new_M = M.swap_right((0, 0))
        >>> print(new_M.from_grid)
        (('2', '*', '3'), ('1', '4', '5'))
        """
        
        grid = self.from_grid
        new_grid = []
        for row in grid:
            new_grid.append(list(row))
        
        new_grid[index[0]][index[1]], new_grid[index[0]][index[1] + 1] = \
                           new_grid[index[0]][index[1] + 1], new_grid[index[0]][
                               index[1]]
        for i in range(len(grid)):
            new_grid[i] = tuple(new_grid[i])
            
        return MNPuzzle(tuple(new_grid), self.to_grid)

    def find_index(self):
        """
        Return a tuple that indicates the position of "*" on the grid

        @type self: MNPuzzle
        @rtype index: tuple[int]
        
        >>> target_grid = (("1", "2", "3"), ("4", "5", "*"))
        >>> start_grid = (("*", "2", "3"), ("1", "4", "5"))
        >>> M = MNPuzzle(start_grid, target_grid)
        >>> M.find_index()
        (0, 0)
        """
        current = self.from_grid
        #find index of "*"
        for x in range(len(current)):
            for y in range(len(current[x])):
                if current[x][y] == "*":
                    index = (x,y)
        return index

    # TODO
    # override is_solved
    # a configuration is solved when from_grid is the same as to_grid
    def is_solved(self):
        """
        Return whether MNPuzzle self is solved.
        
        @type self: MNPuzzle 
        @rtype: bool

        >>> target_grid = (("1", "2", "3"), ("4", "5", "*"))
        >>> start_grid1 = (("*", "2", "3"), ("1", "4", "5"))
        >>> M1 = MNPuzzle(start_grid1, target_grid)
        >>> M1.is_solved()
        False
        >>> start_grid2 = (("1", "2", "3"), ("4", "5", "*"))
        >>> M2 = MNPuzzle(start_grid2, target_grid)
        >>> M2.is_solved()
        True
        """
        return (self.from_grid == self.to_grid)

if __name__ == "__main__":
    import doctest
    doctest.testmod()
    target_grid = (("1", "2", "3"), ("4", "5", "*"))
    start_grid = (("*", "2", "3"), ("1", "4", "5"))
    from puzzle_tools import breadth_first_solve, depth_first_solve
    from time import time
    start = time()
    solution = breadth_first_solve(MNPuzzle(start_grid, target_grid))
    end = time()
    print("BFS solved: \n\n{} \n\nin {} seconds".format(
        solution, end - start))
    start = time()
    solution = depth_first_solve((MNPuzzle(start_grid, target_grid)))
    end = time()
    print("DFS solved: \n\n{} \n\nin {} seconds".format(
        solution, end - start))
