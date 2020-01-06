package net.netnook.repeg.expressions.core;

import net.netnook.repeg.Expression;
import net.netnook.repeg.ExpressionBuilder;
import net.netnook.repeg.OnSuccessHandler;
import net.netnook.repeg.expressions.CompoundExpression;
import net.netnook.repeg.expressions.ExpressionBase;
import net.netnook.repeg.expressions.ExpressionBuilderBase;
import net.netnook.repeg.expressions.RootContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Sequence expression i.e. '{@code (a b c)}'.
 * <p>
 * This expression handles an sequence of sub-expressions, attempting to match each sub-expression
 * in sequence and returning a match if all sub-expressions match.
 * <p>
 * This expression has no default {@link OnSuccessHandler}.
 */
public final class Sequence extends ExpressionBase implements CompoundExpression {

    /**
     * Create a new {@link Sequence} expression with the specified sub-expressions.
     * <p>
     * A Sequence matches if all it's sub-expressions match in the specified order.
     *
     * @param expressions sub-expressions to match in sequence
     * @return the new {@link Sequence} expression.
     */
    public static Builder of(ExpressionBuilder... expressions) {
        return new Builder().expressions(expressions);
    }

    public static class Builder extends ExpressionBuilderBase {
        private ExpressionBuilder[] expressions;

        public Builder expressions(ExpressionBuilder[] expressions) {
            this.expressions = expressions;
            return this;
        }

        @Override
        public Builder onSuccess(OnSuccessHandler onSuccess) {
            super.onSuccess(onSuccess);
            return this;
        }

        @Override
        protected Sequence doBuild() {
            return new Sequence(this);
        }
    }

    private final Expression[] expressions;

    private Sequence(Builder builder) {
        super(builder.getOnSuccess());
        this.expressions = build(builder.expressions);
    }

    @Override
    public List<Expression> parts() {
        return Arrays.asList(expressions);
    }

    @Override
    public String buildGrammar() {
        return Stream.of(expressions) //
                .map(Expression::buildGrammar) //
                .collect(Collectors.joining(" ", "(", ")"));
    }

    @Override
    protected boolean doParse(RootContext context, int startPosition, int startStackIdx) {
        for (Expression expression : expressions) {
            boolean success = expression.parse(context);
            if (!success) {
                return false;
            }
        }
        return true;
    }
}
