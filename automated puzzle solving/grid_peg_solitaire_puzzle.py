from puzzle import Puzzle


class GridPegSolitairePuzzle(Puzzle):
    """
    Snapshot of peg solitaire on a rectangular grid. May be solved,
    unsolved, or even unsolvable.
    """

    def __init__(self, marker, marker_set):
        """
        Create a new GridPegSolitairePuzzle self with
        marker indicating pegs, spaces, and unused
        and marker_set indicating allowed markers.

        @type marker: list[list[str]]
        @type marker_set: set[str]
                          "#" for unused, "*" for peg, "." for empty
        """
        assert isinstance(marker, list)
        assert len(marker) > 0
        assert all([len(x) == len(marker[0]) for x in marker[1:]])
        assert all([all(x in marker_set for x in row) for row in marker])
        assert all([x == "*" or x == "." or x == "#" for x in marker_set])
        self._marker, self._marker_set = marker, marker_set

    # TODO
    # implement __eq__, __str__ methods
    # __repr__ is up to you
    def __eq__(self, other):
        """
        Return whether GridPegSolitairePuzzle self is equivalent to other.

        @type self: GridPegSolitairePuzzle
        @type other: GridPegSolitairePuzzle | Any
        @rtype: bool

        >>> grid1 = [["*", "*", "*", "*", "*"]]
        >>> grid1.append(["*", "*", ".", "*", "*"])
        >>> grid1.append(["*", "*", "*", "*", "*"])
        >>> g1 = GridPegSolitairePuzzle(grid1, {"*", ".", "#"})
        >>> grid2 = [["*", "*", "*", "*", "*"]]
        >>> grid2.append(["*", "*", ".", "*", "*"])
        >>> grid2.append(["*", "*", "*", "*", "*"])
        >>> g2 = GridPegSolitairePuzzle(grid2, {"*", ".", "#"})
        >>> g1 == g2
        True
        >>> grid3 = [["*", ".", "*", ".", "*"]]
        >>> grid3.append(["*", "*", ".", "*", "*"])
        >>> grid3.append(["*", "*", "*", ".", "*"])
        >>> g3 = GridPegSolitairePuzzle(grid3, {"*", ".", "#"})
        >>> g1 == g3
        False
        """
        return (type(self) == type(other) and
                self._marker == other._marker and
                self._marker_set == other._marker_set)

    def __str__(self):
        """
        Return a human-readable string representation of GridPegSolitairePuzzle
        self.

        >>> grid = [["#", "*", "*", "*", "#"]]
        >>> grid.append(["*", "*", "*", "*", "*"])
        >>> grid.append(["*", "*", ".", "*", "*"])
        >>> grid.append(["*", "*", "*", "*", "*"])
        >>> grid.append(["#", "*", "*", "*", "#"])
        >>> g = GridPegSolitairePuzzle(grid, {"*", ".", "#"})
        >>> print(g)
          * * *   
        * * * * * 
        * * . * * 
        * * * * * 
          * * *   
        """
        output = ""

        for row in self._marker:
            for item in row:
                if item == "#":
                    output += "  "
                else:
                    output += item + " "
            output += "\n"
        return output.strip("\n")
        

    # TODO
    # override extensions
    # legal extensions consist of all configurations that can be reached by
    # making a single jump from this configuration
    def extensions(self):
        """
        Return a list of extensions of GridPegSolitairePuzzle self.

        @type self: GridPegSolitairePuzzle 
        @rtype: list[GridPegSolitairePuzzle]

        >>> grid = [["#", "*", "*", "*", "#"]]
        >>> grid.append(["*", "*", "*", "*", "*"])
        >>> grid.append(["*", "*", "*", "*", "*"])
        >>> grid.append(["*", "*", "*", "*", "*"])
        >>> grid.append(["#", "*", "*", ".", "#"])
        >>> g = GridPegSolitairePuzzle(grid, {"*", ".", "#"})
        >>> extension = g.extensions()
        >>> len(extension)
        2
        """
        extensions = []
        #Find all empty indices for the grid in a list of tuple
        #formatted - (row, column)
        empty_indices = self.empty_indices()
        #Goes through all possible jump for every empty index
        for index in empty_indices:
            extensions.extend(self.possible_jumps(index))
        return extensions
        
    def empty_indices(self):
        """
        Return a list of all empty indices positions of GridPegSolitairePuzzle

        @type self: GridPegSolitairePuzzle
        @rtype: list[tuple[int]]

        >>> grid = [["#", "*", "*", "*", "#"]]
        >>> grid.append(["*", "*", "*", "*", "*"])
        >>> grid.append(["*", "*", ".", ".", "*"])
        >>> grid.append(["*", "*", "*", "*", "*"])
        >>> grid.append(["#", "*", "*", "*", "#"])
        >>> g = GridPegSolitairePuzzle(grid, {"*", ".", "#"})
        >>> g.empty_indices()
        [(2, 2), (2, 3)]
        """
        marker = self._marker

        all_empty = []
        
        for x in range(len(marker)):
            for y in range(len(marker[x])):
                if marker[x][y] == ".":
                    all_empty.append((x,y))

        return all_empty

    def possible_jumps(self, index):
        """
        Return a list of extensions for a single empty space.

        @type self: GridPegSolitairePuzzle
        @type index: tuple(int)
        @rtype: list[GridPegSolitairePuzzle]

        >>> grid = [["#", "*", "*", "*", "#"]]
        >>> grid.append(["*", "*", "*", "*", "*"])
        >>> grid.append(["*", "*", ".", ".", "*"])
        >>> grid.append(["*", "*", "*", "*", "*"])
        >>> grid.append(["#", "*", "*", "*", "#"])
        >>> g = GridPegSolitairePuzzle(grid, {"*", ".", "#"})
        >>> possi_jumps = g.possible_jumps((2,3))
        >>> len(possi_jumps) == 2
        True
        """
        possible = []
        
        #Helper function
        def make_copy(list_):
            """
            Return an identical copy of list_

            @type list_: list[list[obj]]
            @rtype: list[list[obj]]
            """
            copy = []
            for i in range(len(list_)):
                copy.append(list(list_[i]))
            return copy
    
        marker = make_copy(self._marker)
        
        #Convenient numbers
        height, width = len(self._marker), len(self._marker[0])
        row, col = index[0], index[1]
        #Above
        if (row - 2 >=0 and marker[row - 1][col] == "*"
            and marker[row - 2][col] == "*"):
            marker[row][col], marker[row - 2][col] = \
            marker[row - 2][col], marker[row][col]
            marker[row - 1][col] = "."
            possible.append(GridPegSolitairePuzzle(marker, self._marker_set))
            marker = make_copy(self._marker)
        #Below
        if (row + 2 < height and marker[row + 1][col] == "*"
            and marker[row + 2][col] == "*"):
            marker[row][col], marker[row + 2][col] = \
            marker[row + 2][col], marker[row][col]
            marker[row + 1][col] = "."
            possible.append(GridPegSolitairePuzzle(marker, self._marker_set))
            marker = make_copy(self._marker)
        #Left
        if (col - 2 >=0 and marker[row][col - 1] == "*"
            and marker[row][col - 2] == "*"):
            marker[row][col], marker[row][col - 2] = \
            marker[row][col - 2], marker[row][col]
            marker[row][col - 1] = "."
            possible.append(GridPegSolitairePuzzle(marker, self._marker_set))
            marker = make_copy(self._marker)
        #Right
        if (col + 2 < width and marker[row][col + 1] == "*"
            and marker[row][col + 2] == "*"):
            marker[row][col], marker[row][col + 2] = \
            marker[row][col + 2], marker[row][col]
            marker[row][col + 1] = "."
            possible.append(GridPegSolitairePuzzle(marker, self._marker_set))
            marker = make_copy(self._marker)                         
        return possible
    
    # TODO
    # override is_solved
    # A configuration is solved when there is exactly one "*" left
    def is_solved(self):
        """
        Return whether GridPegSolitairePuzzle self is solved.
        
        @type self: GridPegSolitairePuzzle 
        @rtype: bool
        
        >>> grid1 = [["#", "*", "*", "*", "#"]]
        >>> grid1.append(["*", "*", "*", "*", "*"])
        >>> grid1.append(["*", "*", ".", "*", "*"])
        >>> grid1.append(["*", "*", "*", "*", "*"])
        >>> grid1.append(["#", "*", "*", "*", "#"])
        >>> g1 = GridPegSolitairePuzzle(grid1, {"*", ".", "#"})
        >>> g1.is_solved()
        False
        >>> grid2 = [["#", ".", ".", ".", "#"]]
        >>> grid2.append([".", ".", ".", ".", "."])
        >>> grid2.append([".", ".", "*", ".", "."])
        >>> grid2.append([".", ".", ".", ".", "."])
        >>> grid2.append(["#", ".", ".", ".", "#"])
        >>> g2 = GridPegSolitairePuzzle(grid2, {"*", ".", "#"})
        >>> g2.is_solved()
        True
        """
        return sum([row.count("*") for row in self._marker]) == 1


if __name__ == "__main__":
    import doctest

    doctest.testmod()
    from puzzle_tools import depth_first_solve

    grid = [["*", "*", "*", "*", "*"],
            ["*", "*", "*", "*", "*"],
            ["*", "*", "*", "*", "*"],
            ["*", "*", ".", "*", "*"],
            ["*", "*", "*", "*", "*"]]
    gpsp = GridPegSolitairePuzzle(grid, {"*", ".", "#"})
    import time

    start = time.time()
    solution = depth_first_solve(gpsp)
    end = time.time()
    print("Solved 5x5 peg solitaire in {} seconds.".format(end - start))
    print("Using depth-first: \n{}".format(solution))
