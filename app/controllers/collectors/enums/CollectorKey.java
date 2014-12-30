package collectors.enums;

/**
 * An interface for Supertyping enums that are used in Collector-Maps as Keys
 * However, when using it with wildcards, the keyword "extends has to be used"
 * --> <? extends CollectorKey>
 * (not "implements")
 *
 * That is just insane!
 */
public interface CollectorKey {}
