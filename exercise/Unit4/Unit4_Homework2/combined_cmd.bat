java -jar U4T2.jar dump -s "self-model.mdj" -t UMLStateMachine -n StateMachine1 > "StateMachine1_part.json"
java -jar U4T2.jar dump -s "self-model.mdj" -t UMLStateMachine -n StateMachine2 > "StateMachine2_part.json"
java -jar U4T2.jar dump -s "self-model.mdj" -t UMLCollaboration -n Collaboration1 > "Collaboration1_part.json"
copy *_part.json data_all.json
