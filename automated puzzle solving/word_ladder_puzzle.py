from puzzle import Puzzle


class WordLadderPuzzle(Puzzle):
    """
    A word-ladder puzzle that may be solved, unsolved, or even unsolvable.
    """

    def __init__(self, from_word, to_word, ws):
        """
        Create a new word-ladder puzzle with the aim of stepping
        from from_word to to_word using words in ws, changing one
        character at each step.

        @type from_word: str
        @type to_word: str
        @type ws: set[str]
        @rtype: None
        """
        (self._from_word, self._to_word, self._word_set) = (from_word,
                                                            to_word, ws)
        # set of characters to use for 1-character changes
        self._chars = "abcdefghijklmnopqrstuvwxyz"

        # TODO
        # implement __eq__ and __str__
        # __repr__ is up to you
    def __eq__(self, other):
        """
        Return whether WordLadderPuzzle self is equivalent to other.

        @type self: WordLadderPuzzle
        @type other: WordLadderPuzzle | Any
        @rtype: bool

        >>> from_word1 = "cost"
        >>> to_word1 = "save"
        >>> ws1 = set(["cast", "case", "cave"])
        >>> wl1 = WordLadderPuzzle(from_word1, to_word1, ws1)
        >>> from_word2 = "cost"
        >>> to_word2 = "save"
        >>> ws2 = set(["cast" , "case", "cave"])
        >>> wl2 = WordLadderPuzzle(from_word2, to_word2, ws2)
        >>> wl1 == wl2
        True
        >>> from_word3 = "cost"
        >>> to_word3 = "save"
        >>> ws3 = set(["cast", "case", "cave", "have"])
        >>> wl3 = WordLadderPuzzle(from_word3, to_word3, ws3)
        >>> wl1 == wl3
        False
        """
        return (type(self) == type(other) and
                self._from_word == other._from_word and
                self._to_word == other._to_word and
                self._word_set == other._word_set)

    def __str__(self):
        """
        Return a human-readable string representation of WordLadderPuzzle
        self.

        >>> from_word = "cost"
        >>> to_word = "save"
        >>> ws = set(["cast", "case", "cave"])
        >>> w = WordLadderPuzzle(from_word, to_word, ws)
        >>> print(w)
        Current: cost
        Goal: save
        """
        return ("Current: {}\nGoal: {}".format(self._from_word,
                                                        self._to_word))
    
        

        # TODO
        # override extensions
        # legal extensions are WordPadderPuzzles that have a from_word that can
        # be reached from this one by changing a single letter to one of those
        # in self._chars
    def extensions(self):
        """
        Return a list of extensions of GridPegSolitairePuzzle self.

        @type self: GridPegSolitairePuzzle
        @rtype: list[GridPegSolitairePuzzle]

        >>> from_word = "cost"
        >>> to_word = "save"
        >>> ws = set(["cast", "most", "case", "cave"])
        >>> w = WordLadderPuzzle(from_word, to_word, ws)
        >>> W1 = list(w.extensions())
        >>> for i in W1: print(i)
        Current: most
        Goal: save
        Current: cast
        Goal: save
        """
        extensions = []
        #convenient names
        from_word, to_word = self._from_word, self._to_word
        #Goes through every word possibility for from_word
        for i in range(len(from_word)):
            #Change letter at position i to all letters
            for letter in self._chars:
                word = list(from_word)
                word[i] = letter
                word = "".join(word)
                #Found word in word set
                if word in self._word_set:
                    extensions.append(WordLadderPuzzle(word, to_word,
                                                       self._word_set - {word}))                         
        return extensions
      

        # TODO
        # override is_solved
        # this WordLadderPuzzle is solved when _from_word is the same as
        # _to_word
    def is_solved(self):
        """
        Return whether GridPegSolitairePuzzle self is solved.
        
        @type self: GridPegSolitairePuzzle
        @rtype: bool
        """
        return (self._from_word == self._to_word)

if __name__ == '__main__':
    import doctest
    doctest.testmod()
    from puzzle_tools import breadth_first_solve, depth_first_solve
    from time import time
    with open("words", "r") as words:
        word_set = set(words.read().split())
    w = WordLadderPuzzle("same", "cost", word_set)
    start = time()
    sol = breadth_first_solve(w)
    end = time()
    print("Solving word ladder from same->cost")
    print("...using breadth-first-search")
    print("Solutions: {} took {} seconds.".format(sol, end - start))
    start = time()
    sol = depth_first_solve(w)
    end = time()
    print("Solving word ladder from same->cost")
    print("...using depth-first-search")
    print("Solutions: {} took {} seconds.".format(sol, end - start))
