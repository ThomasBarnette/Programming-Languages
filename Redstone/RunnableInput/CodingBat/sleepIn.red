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
print |. sleepIn|. true, false .| .|܍  
print |. sleepIn|. false, false .| .|܍  


