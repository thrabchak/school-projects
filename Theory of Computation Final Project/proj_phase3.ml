(*
    Ken Gorab, Tom Hrabchak, Corey Lear
    CSCI 341
    11/7/13
*)

(*--------------------------------------------------------------------*)

(*
    Our custom Algebraic Datatype for a regular expression, consisting
    of a char, a Union, a Concatenation, a Star, or Epsilon. These are
    all custom datatypes that use Cartesian product with regex.
*)
type regex = 
	| Eps
        | Lit of char
	| Union of regex * regex
	| Concat of regex * regex
	| Star of regex;;

(*
    Given a string and an expression as defined using a combination of 
    the above datatypes, this function determines if the string belongs
    in the language recognized by that expression.    
*)
let rec belongs str expr = match expr with
	| Eps -> String.length str == 0
	| Lit a -> String.length str == 1 && str.[0] == a
	| Union (exp1, exp2) -> belongs str exp1 || belongs str exp2
	| Concat (exp1, exp2) -> 
        let result = ref false in
        for i = 0 to (String.length str) do
            let str1 = String.sub str 0 i in
            let str2 = String.sub str i (String.length str - i) in
            result := !result || (belongs str1 exp1 && belongs str2 exp2)
        done;
        !result
    	| Star exp -> star_helper str exp
       
(*
    A helper function necessary to provide an additional layer of recursion,
    this determines if the string given in `belongs` is part of the language.
*)
(*and matches_star = ref false*)
and star_helper str expr =
    let matches_star = ref false in
    if String.length str == 0 then 
        true
    else begin
        for i = 0 to (String.length str) do
            if (belongs (String.sub str 0 i) expr) then
                matches_star := star_helper (String.sub str i (String.length str - i)) expr 
        done;
        !matches_star
    end
    
(*
    Calculates the potential for a string in the language to include an epsilon;
    a union of two expressions has twice the potential than just a single expression.
*)
let rec delta expr = match expr with
    | Eps -> 1
    | Lit a -> 0
    | Union (exp1, exp2) -> delta exp1 + delta exp2
    | Concat (exp1, exp2) -> delta exp1 * delta exp2
    | Star exp1 -> 1

(*
    Calculates all of the single-character prefixes for words in the language recognized
    by a given regular expression and returns a List without duplicates of those chars.
*)
let rec first expr = match expr with
    | Eps -> []
    | Lit a -> [a]
    | Union (exp1, exp2) -> remove_duplicates ((first exp1) @ (first exp2))
    | Concat (exp1, exp2) -> 
	if delta exp1 == 0 then
		first exp1
	else
		remove_duplicates ((first exp1) @ (first exp2))
    | Star exp1 -> first exp1


(*
    Given a list, recursively strip that list of all duplicates in that list.
*)
and remove_duplicates l = 
    remove_duplicates_helper l 0
and remove_duplicates_helper l i =
    if List.length l <= 1 then
        l
    else begin
        let first_char = List.nth l 0 in
        let filtered = List.filter (fun (a : char) -> a != first_char) l in
        let duplicates_removed = remove_duplicates_helper filtered (i + 1) in
        List.append duplicates_removed [first_char]
    end

let rec last expr = match expr with
    | Eps -> []
    | Lit a -> [a]
    | Union (exp1, exp2) -> remove_duplicates(last exp1 @ last exp2)
    | Concat (exp1, exp2) -> 
      if delta exp2 == 0 then
          last exp2
      else
          remove_duplicates ((last exp1) @ (last exp2)) 
    | Star exp1 -> last exp1

and follow expr c = match expr with
    | Eps -> []
    | Lit a -> []
    | Union (exp1, exp2) -> remove_duplicates((follow exp1 c) @ (follow exp2 c))
    | Concat (exp1, exp2) ->
      let new_list = (follow exp1 c) @ (follow exp2 c) in 
      if (List.exists (fun (a : char) -> a == c ) (last exp1) ) then
          remove_duplicates (new_list @ (first exp2))
      else
          remove_duplicates new_list
    | Star exp1 -> 
      let new_list = (follow exp1 c) in
      if (List.exists (fun (a : char) -> a == c ) (last exp1) ) then
          remove_duplicates (new_list @ (first exp1))
      else
          new_list

(*--------------------------------------------------------------------------*)

let rec get_first_Lit expr = match expr with
	| Eps -> Eps
	| Lit a -> Lit a
	| Union (exp1, exp2) -> 
		if exp1 = Eps
		then get_first_Lit exp2
		else get_first_Lit exp1
	| Concat (exp1, exp2) ->
		if exp1 = Eps
		then get_first_Lit exp2
		else get_first_Lit exp1
	| Star exp1 -> get_first_Lit exp1

let rec remove_first_Lit expr = match expr with
	| Eps -> Eps 
	| Lit a -> Eps
	| Union (exp1, exp2) -> 
		if exp1 = Eps
		then remove_first_Lit exp2
		else Union (remove_first_Lit exp1, exp2)
	| Concat (exp1, exp2) ->
		if exp1 = Eps
		then remove_first_Lit exp2
		else Concat (remove_first_Lit exp1, exp2)
	| Star exp1 -> remove_first_Lit exp1

let rec get_marked_Lits expr n = 
	if expr == Eps
	then []
	else [(get_first_Lit expr, n)] @ get_marked_Lits (remove_first_Lit expr) (n + 1)

(*
let rec get_transitions expr marked_list = match marked_list with
	| [] -> []
	| (Lit a, i) :: xs -> [(i, follow expr a)] @ get_transitions expr xs

let make_automaton expr = 
	get_transitions expr (get_marked_Lits expr 1)
*)

(*-----------------------------------------------------------*)

type markedRegex = 
        | MEps
        | MLit of char * int
        | MUnion of markedRegex * markedRegex
        | MConcat of markedRegex * markedRegex
        | MStar of markedRegex;;

let rec mbelongs str expr = match expr with
	| MEps -> String.length str == 0
	| MLit (a,i) -> String.length str == 1 && str.[0] == a
	| MUnion (exp1, exp2) -> mbelongs str exp1 || mbelongs str exp2
	| MConcat (exp1, exp2) -> 
        let result = ref false in
        for i = 0 to (String.length str) do
            let str1 = String.sub str 0 i in
            let str2 = String.sub str i (String.length str - i) in
            result := !result || (mbelongs str1 exp1 && mbelongs str2 exp2)
        done;
        !result
    	| MStar exp -> mstar_helper str exp

and mstar_helper str expr =
    let matches_star = ref false in
    if String.length str == 0 then 
        true
    else begin
        for i = 0 to (String.length str) do
            if (mbelongs (String.sub str 0 i) expr) then
                matches_star := mstar_helper (String.sub str i (String.length str - i)) expr 
        done;
        !matches_star
    end

let rec mdelta expr = match expr with
    | MEps -> 1
    | MLit (a,i) -> 0
    | MUnion (exp1, exp2) -> mdelta exp1 + mdelta exp2
    | MConcat (exp1, exp2) -> mdelta exp1 * mdelta exp2
    | MStar exp1 -> 1

let rec getMarkedExprTuple expr n = match expr with
     | Eps -> (MEps,n)
     | Lit a -> (MLit (a, n),n+1)
     | Union (exp1, exp2) -> 
        let x = getMarkedExprTuple exp1 n in
        let new_n = snd x in
        let y = getMarkedExprTuple exp2 new_n in
        (MUnion ((fst x),(fst y)), (snd y)) 
    | Concat (exp1, exp2) -> 
        let x = getMarkedExprTuple exp1 n in
        let new_n = snd x in
        let y = getMarkedExprTuple exp2 new_n in
        (MConcat ((fst x),(fst y)), (snd y)) 
     | Star exp -> 
        let x = getMarkedExprTuple exp n in
        (MStar (fst x),(snd x))

let getMarkedExpr expr = fst (getMarkedExprTuple expr 1)

let rec mfirst expr = match expr with
    | MEps -> []
    | MLit (a,i) -> [(a,i)]
    | MUnion (exp1, exp2) -> (mfirst exp1) @ (mfirst exp2)
    | MConcat (exp1, exp2) -> 
	if mdelta exp1 == 0 then
		mfirst exp1
	else
		mfirst exp1 @ mfirst exp2
    | MStar exp1 -> mfirst exp1

let rec mlast expr = match expr with
    | MEps -> []
    | MLit (a,i) -> [(a,i)]
    | MUnion (exp1, exp2) -> mlast exp1 @ mlast exp2
    | MConcat (exp1, exp2) -> 
      if mdelta exp2 == 0 then
          mlast exp2
      else
          mlast exp1 @ mlast exp2 
    | MStar exp1 -> mlast exp1

let rec mfollow expr (a,i) = match expr with
    | MEps -> []
    | MLit (b,n) -> []
    | MUnion (exp1, exp2) -> (mfollow exp1 (a,i)) @ (mfollow exp2 (a,i))
    | MConcat (exp1, exp2) ->
      let new_list = mfollow exp1 (a,i) @ mfollow exp2 (a,i) in 
      if (List.exists (fun ((b,n) : char * int) -> a == b && i == n ) (mlast exp1) ) then
          new_list @ mfirst exp2
      else
          new_list
    | MStar exp1 -> 
      let new_list = (mfollow exp1 (a,i)) in
      if (List.exists (fun ((b,n) : char * int) -> a == b && i == n ) (mlast exp1) ) then
          new_list @ mfirst exp1
      else
          new_list

let rec getStates exp = match exp with
    | MEps -> []
    | MLit (a,i) -> [(a,i)]
    | MUnion (exp1, exp2) -> getStates exp1 @ getStates exp2
    | MConcat (exp1, exp2) -> getStates exp1 @ getStates exp2
    | MStar exp1 -> getStates exp1

let rec getTransitionsFromStates exp states = match states with  
    | [] -> []
    | (a,i)::xs -> [(i, mfollow exp (a,i))] @ getTransitionsFromStates exp xs

let getTransitions exp = getTransitionsFromStates exp (getStates exp);;

(*-----------------------------------------------------------*)

(*
    Designated variables used for testing.
*)
let e = Concat(Lit 'a', Lit 'a');;
let expr = Concat (Concat (Lit 'a', Union (Lit 'b', Lit 'a')), Lit 'd');;
let str = "bbabbaa";;
let m = getMarkedExpr expr;;
let answer = mlast m;;
