package com.myorg;

import software.amazon.awscdk.services.sqs.Queue;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
// import software.amazon.awscdk.Duration;
// import software.amazon.awscdk.services.sqs.Queue;

public class TmpStack extends Stack {
    public TmpStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public TmpStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);




    }
}
