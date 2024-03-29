# Redstone Language
The goal of this project is to created a language modeled after the functionality of minecraft redstone. Since this would be very difficult to do and might not fit well within the scope of this class, it will probably just be mostly themed around the game of minecraft.

Minecraft is a game done entirely with blocks, and therefore curvy special characters like parentheses ```()``` and braces ```{}``` are not supported in the Redstone language. Instead, these characters are replaced with brackets ```[]``` and verticle bars ```|.  .|``` when applicable. Instead of a traditional semicolon, the '''܍''' character will be used because it looks like a piece of redstone dust. 


## Variables
Although the redstone system is turing complete, it doesn't really have a way to store variables.
For ease of use however, the redstone languae will support dynamically typed variables.
Start define a variable using the ``` /summon ``` keyword, in the pattern ```/summon name = value```

An example of variable initialzation, declaration, and assignment would be:
```
  /summon x = 5܍ 
  x = "creeper"܍ 
  x = true܍ 
 ```
 
Variables can be deleted in redstone using the ```/kill``` keyword. I.e. ```/kill x``` would make it so x cannot be redifined or used unless it is /summoned again

## Functions
There will be 3 different types of functions:
1) The ```hopper``` keyword denotes a function that takes in any number parameters of any type and returns nothing (void)
2) the ```dropper``` keyword denotes a function that can return any type, but takes in no parameters
3) the ```hopperdropper``` keyword denotes a function that take in any number paramters and returns any value

All ```dropper``` functions will return values either implicitly (using the last line of the function) or by using the ```drop``` keyword

Here is an example of a ```hopperdropper``` function and a ```dropper``` function:
```
hopperdropper prod|. x, y .|[
   drop *x_y܍ 
]

dropper pi[
   drop 3.1415976542175628173564764266545267115534546܍ 
]
```
## Control Flow
### Looping
There will be two types of looping supported in redstone.

The first type is the ```repeater``` loop, which will vaguely resemble a ```for``` loop. Because repeaters have 4 tick settings in minecraft, the only parameter in repeater loops will be an Integer between 1-4 that will determine how many times the loop will run. The tick will increase by 1 until it is the value as the paramter. Heres an example of nested repeater loops in the redstone language:
```
/summon count = 0
repeater|. 4 .| [ 
  repeater|. 2 .| [
  ++count܍  
  ]
]
print|. count .|܍ 
```
The expected output of this snippet would be ```8```.

The second type of iteration supported is the ```comparator``` loop, which is similar to a ```while(true)``` loop. This loop will take in no parameters and will run indefinetly until the ```mine``` command is run. Here is an example of a comparator:
```
/summon count = 0
comparator [ 
  if|. count <> 10 .| [ 
    mine܍ 
   ]  
   count++܍ 
]
```
This loop would run 10 times up to the point where count = 10, at which point it would exit out of the loop following the ```mine``` command.
### Conditionals
  Conditional statements will follow the standard, ```if``` ```else if``` and ```else``` logic, but with the keywords ```if```, ```eif```, and ```ese```
  
  Condional logic will be done by spelling out ```or``` and ```and```, and will follow the order of operations using lines ```||``` as parentheses

## Operators
The redstone language is prefixed, so there wont be tradiational binary operators but rather "nth-ary" operators with an undefined amount of terms.
### Unary assignment
These are mostly self explainatory, either adding or subtracting 1 to a variable:
``` ++, --```
### "Nth-ary"
Because the redstone language is prefixed, all operators can have any n number of terms. To support this, the tilde ```~``` character will be used to denote between different numbers.

Here is an example of "nth-ary" addition in the language.
```
+5~3~7~4܍ 
```
```+, -, *, /```
##"Nth-ary" assignment
```*-, /=, +=, -=```

### Others?
```**, /./, %%```
These are number squared, square root of a number, and number of digits in a number respectivly.

## Comparators
### Magnitude
Can support ```>```, ```<```, ```>=```, and ```<=```
### Equality
Supports ```<>``` for equals, ```><``` for inequals, and has a unique feature:
```
number <value> number 
```
This checks if the numbers are within value from eachother.
### Logic
Both  ```and``` and ```or``` keywords are used between conditional expressions to denote standard logic.
Eamples:

```
+5~3 <> x or -5~3 >< x܍

y and +5~1 <x> 15܍ 
```

## Collections
### Arrays
Lists will be the only collection supported in redstone.
There are two ways to add elements to a list.
Way #1 (single quotes and commas):
``` 
/summon x = '1, 2, 3'܍ 
```
Way #2 (.add method):
```
/summon x = ''܍ 
x.add|. 2 .|܍ 
print|. x .|܍ 
```
the console would then print out ``` '2' ```. The   ```list.remove|. .|``` method will also be supported.
To access from a list, you can use the ```@```symbol to find the value at the given index:
```
/summon x = '1, 2, 3'
show|. x@1 .|
```
This would print ```2``` to the console
## Built-In Functions
### Printing to the console
All priting to the console will be done through the ```print|. .|``` function

# Keywords/Operators
| Syntax      | Description |
| ----------- | ----------- |
| ```/summon```      | creates a new variable      |
| ```/kill```   | "deletes" an already existing variable        |
| ```hopper```  | makes a function w paramters and doesn't return a value        |
| ```dropper```   | makes a function w/out paramters and doesn't return a value       |
| ```hopper dropper```  | makes a function with parameters and returns a value       |
| ```repeater```   | a for loop that goes from 0 to a specified value 1-4        |
| ```comparator```   | a while(true) loop        |
| ```show```   | prints arguments to the console        |
| ```if```      | standard if conditional      |
| ```eif```     | else if conditional    |
| ```ese```      | else conditional      |
| ```+```   | adds any numbers following it together (separated by underscores)        |
| ```-```   | subtracts the first number following it from all the numbers following it added together (separated by underscores)       |
| ```++```  | adds 1 to the first variable that follows it        |
| ```--```  | subracts 1 to the first variable that follows it        |
| ```+=```   | a variable is assigned to the following numbers added together (plus the variable)       |
| ```-=```   | a variable is assigned to its value subtracted from the numbers values added together        |
| ```* ```  | multiplies any numbers following it together      |
| ```/```   | divides the second number following it from the first (only accepts 2 numbers)      |
| ```~```   | connection between 2 values in an "nth-ary" expression     |
| ```**```   | squares the number following it       |
| ```*= ```  | a variable is assigned itself multiplied by the following numbers      |
| ```/=```   | variable is assigned itself divided by the number immediatly followed        |
| ```/./```   | takes the square root of any number       |
| ```>```   | "greater than" comparator       |
| ```<```   | "less than' comparator        |
| ```>=```  | "greater than or equal to" comparator         |
| ```<=```   | "less than or equal to" comparator         |
| ```<>```   | "equals" comparator        |
| ```\<num>```   | "is within num of" comparator        |
| ```><```   | "inequal to" comparator         |
| ```>num<```  | "is not within num of" comparator        |
| ```and```   | logical and      |
| ```or```   | logical or      |
| ```print\|. primary .\| ```   | prints the given primary to the console    |
| ```@```   | access the given index in a given list      |
        
  
  Created in Spring 2023 by Thomas Barnette 
  © 2023 All rights reserved
