// W<:T
type R record {|
    int id;
    int f;
|};

type R1 record {|
    int id;
    int f;
    float d;
|};

type T table<R> & readonly | table<R1> & readonly;
type W table<R1> & readonly;
