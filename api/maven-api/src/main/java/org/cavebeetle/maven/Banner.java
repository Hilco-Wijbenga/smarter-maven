package org.cavebeetle.maven;

/**
 * <pre>
 *  ___                _             __  __
 * / __|_ __  __ _ _ _| |_ ___ _ _  |  \/  |__ _Apache__ _ _
 * \__ \ '  \/ _` | '_|  _/ -_) '_| | |\/| / _` \ V / -_) ' \
 * |___/_|_|_\__,_|_|  \__\___|_|   |_|  |_\__,_|\_/\___|_||_|
 * </pre>
 */
public interface Banner
{
    /**
     * The banner to display when starting the Smarter Maven extension.
     */
    String[] BANNER = new String[] {
        " ___                _             __  __                   ".replace('∖', '\\'),
        "/ __|_ __  __ _ _ _| |_ ___ _ _  |  ∖/  |__ _Apache__ _ _  ".replace('∖', '\\'),
        "∖__ ∖ '  ∖/ _` | '_|  _/ -_) '_| | |∖/| / _` ∖ V / -_) ' ∖ ".replace('∖', '\\'),
        "|___/_|_|_∖__,_|_|  ∖__∖___|_|   |_|  |_∖__,_|∖_/∖___|_||_|".replace('∖', '\\'),
    };
}
