/**
 * Validate function
 */
(function(newDoc, oldDoc, userCtx) {
    if (!newDoc.hasOwnProperty('type')) {
        throw ({
            forbidden : 'must have `type` field.'
        });
    }
});