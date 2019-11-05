//package seedu.address.logic.parser;
//
//
//import static seedu.address.logic.commands.CommandTestUtil.*;
//import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
//import static seedu.address.testutil.TypicalEntries.CATEGORY_FOOD;
//
//import org.junit.jupiter.api.Test;
//import seedu.address.logic.commands.AddCategoryCommand;
//import seedu.address.model.entry.Category;
//import seedu.address.testutil.CategoryBuilder;
//
//public class AddCategoryCommandParserTest {
//    private AddCategoryCommandParser parser = new AddCategoryCommandParser();
//
//    @Test
//    public void parse_allFieldsPresent_success() {
//        Category expectedCategory = new CategoryBuilder(CATEGORY_FOOD).build();
//
//        // whitespace only preamble, Expense
//        assertParseSuccess(parser, PREAMBLE_WHITESPACE + CATEGORY_TYPE_EXPENSE + CATEGORY_NAME_EXPENSE
//                , new AddCategoryCommand(expectedCategory));
//
//        // whitespace only preamble, Income
//        assertParseSuccess(parser, PREAMBLE_WHITESPACE + CATEGORY_TYPE_EXPENSE + CATEGORY_NAME_EXPENSE
//                , new AddCategoryCommand(expectedCategory));
//    }
//
////        // multiple names - last name accepted
////        assertParseSuccess(parser, NAME_DESC_FOOD_EXPENSE + NAME_DESC_CLOTHING_EXPENSE + PHONE_DESC_BOB
////        + EMAIL_DESC_BOB
////                + ADDRESS_DESC_BOB + TAG_DESC_CLOTHING, new AddCommand(expectedPerson));
////
////        // multiple phones - last phone accepted
////        assertParseSuccess(parser, NAME_DESC_CLOTHING_EXPENSE + PHONE_DESC_AMY + PHONE_DESC_BOB + EMAIL_DESC_BOB
////                + ADDRESS_DESC_BOB + TAG_DESC_CLOTHING, new AddCommand(expectedPerson));
////
////        // multiple emails - last email accepted
////        assertParseSuccess(parser, NAME_DESC_CLOTHING_EXPENSE + PHONE_DESC_BOB + EMAIL_DESC_AMY + EMAIL_DESC_BOB
////                + ADDRESS_DESC_BOB + TAG_DESC_CLOTHING, new AddCommand(expectedPerson));
////
////        // multiple addresses - last address accepted
////        assertParseSuccess(parser, NAME_DESC_CLOTHING_EXPENSE + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_AMY
////                + ADDRESS_DESC_BOB + TAG_DESC_CLOTHING, new AddCommand(expectedPerson));
////
////        // multiple tags - all accepted
////        Person expectedPersonMultipleTags = new EntryBuilder(BOB).withTags(VALID_TAG_CLOTHING, VALID_TAG_FOOD)
////                .build();
////        assertParseSuccess(parser, NAME_DESC_CLOTHING_EXPENSE + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
////                + TAG_DESC_FOOD + TAG_DESC_CLOTHING, new AddCommand(expectedPersonMultipleTags));
////    }
////
////    @Test
////    public void parse_optionalFieldsMissing_success() {
////        // zero tags
////        Person expectedPerson = new EntryBuilder(AMY).withTags().build();
////        assertParseSuccess(parser, NAME_DESC_FOOD_EXPENSE + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY,
////                new AddCommand(expectedPerson));
////    }
////
////    @Test
////    public void parse_compulsoryFieldMissing_failure() {
////        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);
////
////        // missing name prefix
////        assertParseFailure(parser, VALID_DESC_CLOTHING_EXPENSE + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
////                expectedMessage);
////
////        // missing phone prefix
////        assertParseFailure(parser, NAME_DESC_CLOTHING_EXPENSE + VALID_PHONE_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
////                expectedMessage);
////
////        // missing email prefix
////        assertParseFailure(parser, NAME_DESC_CLOTHING_EXPENSE + PHONE_DESC_BOB + VALID_EMAIL_BOB + ADDRESS_DESC_BOB,
////                expectedMessage);
////
////        // missing address prefix
////        assertParseFailure(parser, NAME_DESC_CLOTHING_EXPENSE + PHONE_DESC_BOB + EMAIL_DESC_BOB + VALID_ADDRESS_BOB,
////                expectedMessage);
////
////        // all prefixes missing
////        assertParseFailure(parser, VALID_DESC_CLOTHING_EXPENSE + VALID_PHONE_BOB + VALID_EMAIL_BOB
////        + VALID_ADDRESS_BOB,
////                expectedMessage);
////    }
////
////    @Test
////    public void parse_invalidValue_failure() {
////        // invalid name
////        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
////                + TAG_DESC_FOOD + TAG_DESC_CLOTHING, Description.MESSAGE_CONSTRAINTS);
////
////        // invalid phone
////        assertParseFailure(parser, NAME_DESC_CLOTHING_EXPENSE + INVALID_PHONE_DESC + EMAIL_DESC_BOB
// + ADDRESS_DESC_BOB
////                + TAG_DESC_FOOD + TAG_DESC_CLOTHING, Phone.MESSAGE_CONSTRAINTS);
////
////        // invalid email
////        assertParseFailure(parser, NAME_DESC_CLOTHING_EXPENSE + PHONE_DESC_BOB + INVALID_EMAIL_DESC
// + ADDRESS_DESC_BOB
////                + TAG_DESC_FOOD + TAG_DESC_CLOTHING, Email.MESSAGE_CONSTRAINTS);
////
////        // invalid address
////        assertParseFailure(parser, NAME_DESC_CLOTHING_EXPENSE + PHONE_DESC_BOB + EMAIL_DESC_BOB
// + INVALID_ADDRESS_DESC
////                + TAG_DESC_FOOD + TAG_DESC_CLOTHING, Address.MESSAGE_CONSTRAINTS);
////
////        // invalid tag
////        assertParseFailure(parser, NAME_DESC_CLOTHING_EXPENSE + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
////                + INVALID_TAG_DESC + VALID_TAG_CLOTHING, Tag.MESSAGE_CONSTRAINTS);
////
////        // two invalid values, only first invalid value reported
////        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC,
////                Description.MESSAGE_CONSTRAINTS);
////
////        // non-empty preamble
////        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_CLOTHING_EXPENSE + PHONE_DESC_BOB + EMAIL_DESC_BOB
////                + ADDRESS_DESC_BOB + TAG_DESC_FOOD + TAG_DESC_CLOTHING,
////                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
////    }
//}
