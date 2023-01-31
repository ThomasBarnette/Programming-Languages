# Redstone Language
The goal of this project is to created a game modeled after the functionality of minecraft redstone. Since this would be very difficult to do and might not fit well within the scope of this class, it will probably just be mostly themed around the game of minecraft.
## Variables
Although the redstone system is turing complete, it doesn't really have a way to store variables.
For ease of use however, the redstone languae will support variables, and similar to python, they will not have any type associated with them.
Start define a variable using the ``` /summon ``` keyword, in the pattern ```/summon name = value```

An example of variable initialzation, declaration, and assignment would be:
```
  /summon x = 5
  x = "creeper"
  x = true
 ```
 
Variables can be deleted in redstone using the ```/kill``` keyword. I.e. ```/kill x``` would make it so x cannot be redifined or used unless it is /summoned again

## Functions
There will be 3 different types of functions:
1) The ```hopper``` keyword denotes a function that takes in any number parameters of any type and returns nothing
2) the ```dropper``` keyword denotes a function that can return any type, but takes in no parameters
3) the ```hopper dropper``` keyword denotes a function that take in any number paramters and returns any value

All ```dropper``` functions will return values either implicitly (using the last line of the function) or by using the ```drop``` keyword

Here is an example of a ```hopper dropper``` function and a ```dropper``` function:
```
hopper dropper prod|x, y|[
   drop x*y
]

dropper pi[
   drop 3.1415976542175628173564764266545267115534546
]
```
## Control Flow
### Looping
There will be two types of looping supported in redstone.

The first type is the ```repeater``` loop, which will vaguely resemble a ```for``` loop. Because repeaters have 4 tick settings in minecraft, the only parameter in repeater loops will be an Integer between 1-4 that will determine how many times the loop will run. The tick will increase by 1 until it is the value as the paramter. Heres an example of nested repeater loops in the redstone language:
```
/summon count = 0
repeater|tick=4| [ 
  repeater|t = 2| [
  count++
  ]
]
console.show(count)
```
The expected output of this snippet would be ```8```.

The second type of iteration supported is the ```comparator``` loop, which is similar to a ```while``` loop. This loop will take in no parameters and will run indefinetly until the ```mine``` command is run. Here is an example of a comparator:
```
/summon count = 0
comparator [ 
  if(count -==- 10) [ 
    mine 
   ]  
   count++
]
```
This loop would run 10 times up to the point where count = 10, at which point it would exit out of the loop following the ```mine``` command.
### Conditionals
  Conditional statements will follow the standard, ```if``` ```else if``` and ```else``` logic, but with the keywords ```if```, ```eif```, and ```ese```

## Operators
Unary
Binary
Ternary
Others?

## Comparators
Magnitude
Equality
Others?

## Collections
Arrays and/or Lists
How to create, modify, and access them and their elements
## Built-In Functions
Which will you include in your language? (You must include some form of print at a minimum)

  
  Created in Spring 2023 by Thomas Barnette 
  © 2023 All rights reserved
