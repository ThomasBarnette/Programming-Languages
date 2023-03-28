hopper largeTest|. x .| [
    if|. x <> 5 .| [
        /summon y܍
    ]
]

dropper dropTest[
    drop 3܍
]

hopperdropper bigFunction|. x, y, z .| [
    if|. x >< z .| [
        /summon a܍
    ]
    drop z܍
]

hopper thisWillError|. z .| [
    drop "oh no"܍
]