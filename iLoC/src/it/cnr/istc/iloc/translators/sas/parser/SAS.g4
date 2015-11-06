grammar SAS;

compilation_unit
    :   version_section metric_section variables_section mutex_section initial_state_section goal_section operator_section axiom_section;

version_section
    :   'begin_version' NUMBER 'end_version';

metric_section
    :   'begin_metric' NUMBER 'end_metric';

variables_section
    :   n_vars=NUMBER variable+;

variable
    :   'begin_variable' var_name=NAME layer=NUMBER range=NUMBER atom+ NUMBER 'end_variable';

atom
    :   'Atom' atom_name=NAME '(' (par=NAME (',' par=NAME)*)? ')'              #positive_atom
    |   'NegatedAtom' atom_name=NAME '(' (par=NAME (',' par=NAME)*)? ')'       #negative_atom
    |   '<none of those>'                                                      #none_atom;

mutex_section
    :   n_mutexes=NUMBER mutex*;

mutex
    :   'begin_mutex_group' size=NUMBER (var_value)+ 'end_mutex_group';

initial_state_section
    :   'begin_state' NUMBER+ 'end_state';

goal_section
    :   'begin_goal' size=NUMBER (var_value)+ 'end_goal';

operator_section
    :   n_operators=NUMBER operator+;

operator
    :   'begin_operator' operator_name=NAME operator_pars=NAME* n_prevail=NUMBER (prevail=var_value)* n_effects=NUMBER (effect)* cost=NUMBER 'end_operator';

effect
    :   n_conditions=NUMBER var_value+ value=NUMBER;

axiom_section
    :   n_rules=NUMBER rule*;

rule
    :   'begin_rule' size=NUMBER conditions=var_value+ condition=var_value value=NUMBER'end_rule';

var_value
    :   var=NUMBER value=NUMBER;

NAME
    :   ('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9'|'-'|'_')*;

NUMBER
    :   [0-9]+ ('.' [0-9]+)?;

COMMENT
    : ';' .*? '\r'? '\n' -> skip ;

WS
    :  [ \r\t\u000C\n]+ -> skip;
