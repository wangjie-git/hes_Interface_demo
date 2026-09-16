# HES interface documentation

Correction dated 2026-09-16, preserving the original interface organization.

1. [Measurements Data Outgoing Interface](KSY_Data_Interchange_Interface.md): KSY
   pushes measured results to the receiving system. This is the main Dizar integration.
2. Basic data synchronization: [Model A](02b-Basic-Data-Synchronization-Model-A.md)
   and [Model B](02c-Basic-Data-Synchronization-Model-B.md) concern organizations and
   doctors. They do not define laboratory worklists.
3. [Resident information download](03-Resident-Information-Download.md): reads
   resident information from KSY; it is not a LIS-to-device patient ingestion API.

The original section numbering is retained. Historical and private-deployment
fields are distinguished from the reviewed standard APP paths in section 4.3 of
the measurement document. The [implementation notes](Customer_Clarification_Closure.md) trace Q01-Q43 to this demo.
The APP upload schema is an internal upstream interface, not the LIS receiver schema.
