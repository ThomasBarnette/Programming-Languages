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