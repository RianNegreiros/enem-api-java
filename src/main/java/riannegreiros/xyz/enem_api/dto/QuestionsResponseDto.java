package riannegreiros.xyz.enem_api.dto;

import java.util.List;

public class QuestionsResponseDto {
    private MetadataDto metadata;
    private List<QuestionDetailsDto> questions;

    public QuestionsResponseDto(MetadataDto metadata, List<QuestionDetailsDto> questions) {
        this.metadata = metadata;
        this.questions = questions;
    }

    public MetadataDto getMetadata() {
        return metadata;
    }

    public List<QuestionDetailsDto> getQuestions() {
        return questions;
    }

    public static class MetadataDto {
        private int limit;
        private int offset;
        private int total;
        private boolean hasMore;

        public MetadataDto(int limit, int offset, int total) {
            this.limit = limit;
            this.offset = offset;
            this.total = total;
            this.hasMore = (offset + limit) < total;
        }

        public int getLimit() {
            return limit;
        }

        public int getOffset() {
            return offset;
        }

        public int getTotal() {
            return total;
        }

        public boolean isHasMore() {
            return hasMore;
        }
    }
}
