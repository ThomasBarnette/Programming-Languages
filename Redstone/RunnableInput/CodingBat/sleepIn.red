*-
--- Coding bat ---
The parameter weekday is true if it is a weekday, and the parameter vacation is true if we are on vacation. 
We sleep in if it is not a weekday or we're on vacation. Return true if we sleep in. 
-*

hopperdropper sleepIn|. weekday, vacation .|[
    /summon bool = |. weekday >< true or vacation <> true .|܍
    drop bool܍
]

print |. sleepIn|. true, true .| .|܍  

hopperdropper within|. num1, num2 .|[
    /summon bool = |. num1 <5> num2 .|܍
    drop bool܍
]

print |. within|. 15, 11 .| .|܍ 


hopperdropper sumsquared|. num1, num2 .|[
    /summon int = +num1~num2܍
    int = **int܍
    drop int܍
]

print |. sumsquared|. 5, 7 .| .|܍  