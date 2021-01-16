#Description

GoodNotes Technical Challenge : https://github.com/GoodNotes/interviews/blob/master/software-engineering.md.

Similar to LWW-Element-Set, the dictionary variant will store a timestamp for each key-value pair. In addition to the add and remove operations, the dictionary variant will also allow updating the value of a key. There should be a function to merge two dictionaries. Test cases should be clearly written and document what aspect of CRDT they test. We recommend you to spend no more than 4 hours on this challenge. The provided readings should be sufficient to understand LWW-Element-Set and CRDT on a high level. You are welcome to dig deeper on those but we expect you to come up with the implementation yourself without any help from other open sourced implementations.

#Edge cases

######Should prefer remove operation if the timestamp of both add and remove are same.

######update operation adds key with null value if old key does not exist or has been already deleted in dictionary.

 
