DROP TABLE IF EXISTS mutant;
CREATE TABLE mutant
(
    dna       VARCHAR(500) NOT NULL,
    is_mutant INTEGER(1)   NOT NULL,
    PRIMARY KEY (dna)
);