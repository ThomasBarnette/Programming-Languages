*-
testing condionals. Output should look like:

yipee!
no way
hihihi
hihi
hi

-*

/summon y = 5܍
if|. y >< 5 .| [
    print|. y .|܍
]

eif|. y <3> 4 .|[
    print|. "yipee!" .|܍
] 

ese [
    print|. "oh no" .|܍
]

if|. |. *y~5~2 .| <> 50 .|[
    print|. "no way" .|܍
]

if|. y >< 4 .| [
    repeater|. 3 .|[
        print|. |. *tick~"hi" .| .|܍
    ]
]
