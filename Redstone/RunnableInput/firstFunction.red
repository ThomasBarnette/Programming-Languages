hopper myFunc|. y, x .| [
    print |. "hello" .|܍
    /summon z܍
    z = *3~5~5܍
    z = **z܍
    /summon test܍
    print|. z .|܍
    print|. x .|܍
    print|. y .|܍
    test = /z~x~y܍
    print|. test .|܍
]

myFunc|. 3, 5 .|܍

repeater|. 3 .| [
    print|. tick .|܍
]


/summon y܍
y = 5܍
comparator[
    if|. y <> 5 .| [
        mine ܍
    ]
    print|. y .|܍
    y = -y~1܍
]
 
