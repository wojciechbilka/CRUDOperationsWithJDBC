package GenericCRUD;

public enum RelationshipType {
    ONE_TO_ONE {
        public RelationshipType getOpposite() {
            return ONE_TO_ONE;
        }
    },
    ONE_TO_MANY{
        public RelationshipType getOpposite() {
            return MANY_TO_ONE;
        }
    },
    MANY_TO_ONE{
        public RelationshipType getOpposite() {
            return ONE_TO_MANY;
        }
    },
    MANY_TO_MANY{
        public RelationshipType getOpposite() {
            return MANY_TO_MANY;
        }
    };

    public abstract RelationshipType getOpposite();
}
