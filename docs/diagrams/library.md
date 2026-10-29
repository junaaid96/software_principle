# Library system diagrams (Mermaid)

GitHub renders these diagrams automatically.

## Class diagram
```mermaid
classDiagram
  class Book { +Isbn isbn +String title +String author }
  class Isbn { <<value object>> +String value }
  class BookCopy { -String copyId -Status status +checkOut() +checkIn() +markLost() }
  class Member { +String memberId +String name +int maxLoans }
  class Loan { +LocalDate borrowedOn +LocalDate dueOn +fineOn(date, perDay) long }
  class LibraryService { +borrow(Member, BookCopy) Loan +giveBack(BookCopy) long }
  class LoanRepository { <<interface>> +save() +remove() +findActiveByCopy() +findActiveByMember() }
  class InMemoryLoanRepository
  Book *-- Isbn
  BookCopy --> Book
  Loan --> Member
  Loan --> BookCopy
  LibraryService --> LoanRepository
  LoanRepository <|.. InMemoryLoanRepository
```

## Sequence diagram: UC-1 Borrow book
```mermaid
sequenceDiagram
  actor Librarian
  participant S as LibraryService
  participant R as LoanRepository
  participant C as BookCopy
  Librarian->>S: borrow(member, copy)
  S->>R: findActiveByMember(memberId)
  R-->>S: active loans
  alt limit reached
    S-->>Librarian: IllegalStateException
  else within limit
    S->>C: checkOut()
    C-->>S: ok (or IllegalStateException if not AVAILABLE)
    S->>R: save(loan)
    S-->>Librarian: loan (due in 14 days)
  end
```

## State diagram: BookCopy
```mermaid
stateDiagram-v2
  [*] --> AVAILABLE
  AVAILABLE --> ON_LOAN: checkOut()
  ON_LOAN --> AVAILABLE: checkIn()
  AVAILABLE --> LOST: markLost()
  ON_LOAN --> LOST: markLost()
```

## Activity diagram: UC-2 Return book
```mermaid
flowchart TD
  A([Copy returned]) --> B{Active loan exists?}
  B -- no --> X([Reject])
  B -- yes --> C[checkIn copy]
  C --> D[Remove loan]
  D --> E{Returned after due date?}
  E -- yes --> F[Fine = late days x 10]
  E -- no --> G[Fine = 0]
  F --> H([Done])
  G --> H
```
