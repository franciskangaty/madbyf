import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public final class PublicationCommentCommands {

    public record SubmitComment(
            @TargetAggregateIdentifier UUID commentId,
            UUID publicationId,
            UUID authorId,
            UUID parentCommentId,
            String content
    ) {}

    public record EditComment(
            @TargetAggregateIdentifier UUID commentId,
            String content
    ) {}

    public record HideComment(
            @TargetAggregateIdentifier UUID commentId,
            String reason
    ) {}

    public record RestoreComment(
            @TargetAggregateIdentifier UUID commentId
    ) {}

    public record PinComment(
            @TargetAggregateIdentifier UUID commentId
    ) {}

    public record ReportComment(
            @TargetAggregateIdentifier UUID commentId,
            String reason
    ) {}

    public record ModerateComment(
            @TargetAggregateIdentifier UUID commentId,
            String moderationAction
    ) {}

    public record DeleteComment(
            @TargetAggregateIdentifier UUID commentId
    ) {}
}