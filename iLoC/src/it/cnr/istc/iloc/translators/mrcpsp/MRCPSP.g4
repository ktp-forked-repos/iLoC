grammar MRCPSP;

@parser::members {
    int n_real_activities;
    int n_renewable_resources;
    int n_nonrenewable_resources;
    int n_doubly_constrained_resources;
    int[] n_modes;
    int[] n_direct_successors;
}

compilation_unit
    :   header activities resource_usages capacities;

header
@after {
        n_modes = new int[n_real_activities + 2];
        n_direct_successors = new int[n_real_activities + 2];
}
    :   n_real_activities=positive_number {n_real_activities = $n_real_activities.v;}
        n_renewable_resources=positive_number {n_renewable_resources = $n_renewable_resources.v;}
        n_nonrenewable_resources=positive_number {n_nonrenewable_resources = $n_nonrenewable_resources.v;}
        n_doubly_constrained_resources=positive_number {n_doubly_constrained_resources = $n_doubly_constrained_resources.v;};

activities locals [int c_activity = 0]
    :   ({$c_activity < n_real_activities + 2}? {$c_activity++;} activity)*;

activity
@after {
        n_modes[$id.v] = $n_modes.v;
        n_direct_successors[$id.v] = $n_direct_successors.v;
}
    :   id=positive_number
        n_modes=positive_number
        n_direct_successors=positive_number
        direct_successors[$n_direct_successors.v]
        weights*;

direct_successors[int n_successors] locals [int c_successor = 0]
    :   ({$c_successor < $n_successors}? {$c_successor++;} positive_number)*;

weights
    :   '[' number+ ']';

resource_usages locals [int c_activity = 0]
    :   ({$c_activity < n_real_activities + 2}? {$c_activity++;} resource_usage)*;

resource_usage
    :   activity_id=positive_number modes[$activity_id.v];

modes[int activity_id] locals [int c_mode = 0]
    :   ({$c_mode < n_modes[$activity_id]}? {$c_mode++;} activity_mode)*;

activity_mode
    :   mode_id=positive_number activity_duration=positive_number renewable_resources_uses nonrenewable_resources_uses doubly_constrained_resources_uses;

renewable_resources_uses locals [int c_res = 0]
    :   ({$c_res < n_renewable_resources}? {$c_res++;} positive_number)*;

nonrenewable_resources_uses locals [int c_res = 0]
    :   ({$c_res < n_nonrenewable_resources}? {$c_res++;} positive_number)*;

doubly_constrained_resources_uses locals [int c_res = 0]
    :   ({$c_res < n_doubly_constrained_resources * 2}? {$c_res++;} positive_number)*;

capacities
    :   renewable_resources_capacities nonrenewable_resources_capacities doubly_constrained_resources_capacities;

renewable_resources_capacities locals [int c_res = 0]
    :   ({$c_res < n_renewable_resources}? {$c_res++;} positive_number)*;

nonrenewable_resources_capacities locals [int c_res = 0]
    :   ({$c_res < n_nonrenewable_resources}? {$c_res++;} positive_number)*;

doubly_constrained_resources_capacities locals [int c_res = 0]
    :   ({$c_res < n_doubly_constrained_resources * 2}? {$c_res++;} positive_number)*;

positive_number returns [int v]
    :   n=NumericLiteral {$v = Integer.valueOf($n.getText());};

number returns [int v]
    :   (negative='-')? n=NumericLiteral {$v = $negative != null ? -Integer.valueOf($n.getText()) : Integer.valueOf($n.getText());};

LBRACKET
    :	'[';

RBRACKET
    :	']';

MINUS
    :	'-';

NumericLiteral
    :   [0-9]+;

WS
    :  [ \r\t\u000C\n]+ -> skip;